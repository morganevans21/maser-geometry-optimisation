package main.java.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.java.config.Constants;
import main.java.config.GAConfig;
import main.java.model.DomainAssignment;
import main.java.metadata.RunDirectory;

/**
 * Manages CSV and log file writing for optimization runs.
 */
public class CsvManager {

    /**
     * Initializes the run directory and creates the necessary files.
     * This should be called at the start of each optimization run.
     *
     * @param config The GA configuration (used for seed and other parameters)
     * @return The created run directory
     * @throws IOException If there is an error creating the directory or files
     */
    public static File initializeRunDirectory(GAConfig config) throws IOException {
        // Create base results directory if it doesn't exist
        File baseDir = Constants.getResultsBaseDirectory();
        if (!baseDir.exists()) {
            boolean created = baseDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create results base directory: " + baseDir.getAbsolutePath());
            }
        }

        // Create run directory with timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        File runDir = new File(baseDir, "run_" + timestamp);
        if (!runDir.exists()) {
            boolean created = runDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create run directory: " + runDir.getAbsolutePath());
            }
        }

        // Set the current run directory
        RunDirectory.setCurrentRunDirectory(runDir);

        // Create results.csv with initial headers
        File resultsFile = new File(runDir, "results.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(resultsFile)) {
            // Write the headers
            writer.append("Generation,Highest Fp,Average Fp,Dielectric Domains,Air Domains\n");
        }

        // Create log.txt (initially empty, we'll append to it)
        File logFile = new File(runDir, "log.txt");
        if (!logFile.exists()) {
            boolean created = logFile.createNewFile();
            if (!created) {
                throw new IOException("Failed to create log file: " + logFile.getAbsolutePath());
            }
        }

        return runDir;
    }

    /**
     * Appends generation data to the results.csv file in the current run directory.
     *
     * @param generation           The generation number
     * @param bestPurcellFactor    The best purcell factor in this generation
     * @param averagePurcellFactor The average purcell factor in this generation
     * @param assignment           The domain assignment for the best solution
     * @param seed                 The random seed used for this run
     */
    public static void appendGenerationToCsv(
            int generation,
            double bestPurcellFactor,
            double averagePurcellFactor,
            DomainAssignment assignment,
            long seed) {

        File runDir = RunDirectory.getCurrentRunDirectory();
        if (runDir == null) {
            System.err.println("Error: No run directory initialized. Call initializeRunDirectory first.");
            return;
        }

        File resultsFile = new File(runDir, "results.csv");
        // Append to CSV file
        try (BufferedWriter writer = Files.newBufferedWriter(
                resultsFile,
                StandardOpenOption.APPEND)) {
            writer.append(String.format("%d,%f,%f,\"%s\",\"%s\",%d\n",
                    generation,
                    bestPurcellFactor,
                    averagePurcellFactor,
                    assignment.dielectricDomains,
                    assignment.airDomains,
                    seed));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a log entry to the log.txt file in the current run directory.
     *
     * @param generation        The generation number
     * @param bestPurcellFactor The best purcell factor
     * @param seed              The random seed
     * @param assignment        The domain assignment
     */
    public static void appendToLog(
            int generation,
            double bestPurcellFactor,
            long seed,
            DomainAssignment assignment) {

        File runDir = RunDirectory.getCurrentRunDirectory();
        if (runDir == null) {
            System.err.println("Error: No run directory initialized. Call initializeRunDirectory first.");
            return;
        }

        File logFile = new File(runDir, "log.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(
                logFile,
                StandardOpenOption.APPEND)) {
            writer.append(String.format("Generation: %d | Best Purcell Factor: %f | Seed: %d%n",
                    generation, bestPurcellFactor, seed));
            writer.append(String.format("Domains assigned to matDielec: %s%n",
                    assignment.dielectricDomains));
            writer.append(String.format("Domains assigned to matAir: %s%n",
                    assignment.airDomains));
            writer.append("---\n"); // Separator between generations

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
