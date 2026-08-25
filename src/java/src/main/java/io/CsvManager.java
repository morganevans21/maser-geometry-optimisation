package main.java.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import main.java.config.Constants;
import main.java.config.GAConfig;
import main.java.model.DomainAssignment;

public class CsvManager {

    public static void initialiseCsvFiles(GAConfig config) {

        try {
            Files.createDirectories(Constants.RESULTS_DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create results directory.", e);
        }

        // Create .csv file to store the main results (change for desired location)
        try (BufferedWriter writer = Files.newBufferedWriter(Constants.RESULTS_FILE)) {
            // Write the parameters of the GA at the beginning of the file
            writer.append(String.format("# Population Size: %d\n", config.populationSize));
            writer.append(String.format("# Generations: %d\n", config.generations));
            writer.append(String.format("# Mutation Rate: %.2f\n", config.mutationRate));
            writer.append(String.format("# Crossover Rate: %.2f\n", config.crossoverRate));
            // Write the headers
            writer.append("Generation,Highest Fp,Average Fp,Dielectric Domains,Air Domains\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create another .csv file to store miscellaneous results (change for desired
        // location)
        try (BufferedWriter writer = Files.newBufferedWriter(Constants.LOG_FILE)) {
            // Write the parameters of the GA at the beginning of the file
            writer.append(String.format("# Population Size: %d\n", config.populationSize));
            writer.append(String.format("# Generations: %d\n", config.generations));
            writer.append(String.format("# Mutation Rate: %.2f\n", config.mutationRate));
            writer.append(String.format("# Crossover Rate: %.2f\n", config.crossoverRate));
            // Write the headers
            writer.append("Fcalc,alpha,Qfactor,Vnum,Vden,Fp,Time\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendGenerationToCsv(
            int generation,
            double bestPurcellFactor,
            double averagePurcellFactor,
            DomainAssignment assignment) {

        // Append to CSV file
        try (BufferedWriter writer = Files.newBufferedWriter(
                Constants.RESULTS_FILE,
                StandardOpenOption.APPEND)) {
            writer.append(String.format("%d,%f,%f,\"%s\",\"%s\"\n",
                    generation,
                    bestPurcellFactor,
                    averagePurcellFactor,
                    assignment.dielectricDomains,
                    assignment.airDomains));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
