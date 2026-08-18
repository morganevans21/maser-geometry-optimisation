package main.java.simulation;

import java.util.ArrayList;
import java.util.List;

import com.comsol.model.Model;

import main.java.config.GAConfig;
import main.java.model.SimulationResult;
import main.java.simulation.SimulationReader;
import main.java.simulation.StudyRunner;

public class PermittivityTuner {

    public static SimulationResult tunePermittivity(
            Model model,
            SimulationResult simulation,
            GAConfig config) {

        double targetFrequency = config.targetFrequency;

        // Apply iterative scaling for better convergence
        double tolerance = 1e7; // 10 MHz tolerance
        int maxIterations = 10;
        int iteration = 0;

        double currentFrequency = simulation.frequency * 1e9;
        double frequencyDiff = Math.abs(currentFrequency - targetFrequency);
        double frequency = simulation.frequency;

        // Store ε and f history for curve fitting
        List<Double> epsHistory = new ArrayList<>();
        List<Double> freqHistory = new ArrayList<>();

        // Relative permittivity value of dielectric (change with material)
        double initialRelativePermittivity = 316.0;
        double relativePermittivity = initialRelativePermittivity;

        while (frequencyDiff > tolerance && iteration < maxIterations) {
            // Skip invalid frequencies (very low = wrong mode)
            if (frequency < 0.01) {
                System.out.println("Invalid frequency detected — breaking.");
                break;
            }

            epsHistory.add(relativePermittivity);
            freqHistory.add(currentFrequency);

            if (epsHistory.size() >= 3) {
                // Ensure all frequencies are valid before using interpolation
                double y1 = freqHistory.get(iteration - 2);
                double y2 = freqHistory.get(iteration - 1);
                double y3 = freqHistory.get(iteration);

                if (y1 > 1e6 && y2 > 1e6 && y3 > 1e6) {
                    double x1 = epsHistory.get(iteration - 2);
                    double x2 = epsHistory.get(iteration - 1);
                    double x3 = epsHistory.get(iteration);

                    double L1 = ((targetFrequency - y2) * (targetFrequency - y3)) / ((y1 - y2) * (y1 - y3));
                    double L2 = ((targetFrequency - y1) * (targetFrequency - y3)) / ((y2 - y1) * (y2 - y3));
                    double L3 = ((targetFrequency - y1) * (targetFrequency - y2)) / ((y3 - y1) * (y3 - y2));

                    double estimatedEps = L1 * x1 + L2 * x2 + L3 * x3;
                    relativePermittivity = Math.max(100, Math.min(700, estimatedEps));
                } else {
                    System.out.println("Skipping interpolation — invalid frequency data.");
                    break;
                }
            } else {
                // Square-law correction
                double correctionFactor = Math.pow(currentFrequency / targetFrequency, 2);
                correctionFactor = Math.max(0.5, Math.min(2.0, correctionFactor));
                relativePermittivity *= correctionFactor;
            }

            // Apply updated permittivity
            model.material("matDielec").propertyGroup("def").set("relpermittivity",
                    String.valueOf(relativePermittivity));
            model.material("matAir").propertyGroup("def").set("relpermittivity",
                    String.valueOf(relativePermittivity / initialRelativePermittivity));

            // Solve again
            StudyRunner.runStudy(model);

            // Update results
            simulation = readSimulationResults(model);

            frequency = simulation.frequency;
            currentFrequency = frequency * 1e9;
            frequencyDiff = Math.abs(currentFrequency - targetFrequency);
            iteration++;

            System.out.printf("Iteration %d | Frequency: %.6f GHz | Eps: %.3f | Δf: %.2f MHz | Purcell: %.3f%n",
                    iteration, frequency, relativePermittivity, frequencyDiff / 1e6, simulation.purcellFactor);
        }

        // Reject bad results if not converged
        if (frequencyDiff > tolerance) {
            System.out.println("Frequency did not converge — skipping result.");
            return null; // Purcell factor not included
        }

        System.out.println("Converged to target frequency — Purcell: " + simulation.purcellFactor);
        return simulation;
    }
}
