package main.java.ga;

import java.util.List;
import java.util.function.Function;

import com.comsol.model.Model;

import main.java.config.GAConfig;
import main.java.io.jenetics.BitChromosome;
import main.java.io.jenetics.BitGene;
import main.java.io.jenetics.Genotype;
import main.java.model.SimulationResult;
import main.java.simulation.MaterialManager;
import main.java.simulation.PermittivityTuner;
import main.java.simulation.SimulationReader;
import main.java.simulation.StudyRunner;

/**
 * Factory for creating fitness functions that adapt the COMSOL objective to Jenetics Function interface.
 */
public class FitnessFunctionFactory {

    public static Function<Genotype<BitGene>, Double> createFitnessFunction(
            Model model,
            List<Integer> subgridDomains,
            GAConfig config) {

        return gt -> {

            // Extract binary chromosome
            BitChromosome chromosome = (BitChromosome) gt.get(0);

            synchronized (model) { // Ensure only one thread accesses COMSOL at a time

                MaterialManager.updateMaterialAssignments(model, chromosome, subgridDomains);

                StudyRunner.runStudy(model);

                StudyRunner.updateNumericalResults(model);

                SimulationResult simulation = SimulationReader.readSimulationResults(model);

                System.out.println("Purcell before: " + simulation.purcellFactor);

                SimulationResult tunedSimulation = PermittivityTuner.tunePermittivity(
                        model,
                        simulation,
                        config);

                if (tunedSimulation == null) {
                    return 0.0;
                }

                return tunedSimulation.purcellFactor;
            }
        };
    }
}