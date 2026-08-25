package main.java.ga;

import java.util.Arrays;
import java.util.List;

import com.comsol.model.Model;

import main.java.io.CsvManager;
import main.java.io.jenetics.BitChromosome;
import main.java.io.jenetics.BitGene;
import main.java.io.jenetics.engine.EvolutionResult;
import main.java.model.DomainAssignment;
import main.java.util.DomainUtils;

public class GenerationLogger {

    public static void printInitialResults(Model model) { // TODO: Is this in the right place?

        // Retrieve table data from "tbl1"
        // "tbl1" should have already been created in COMSOL (in the "Results" >
        // "Tables" section)
        // It should contain the calculated values for; Eigenfrequency (GHz), Frequency
        // (GHz), Quality factor (1), emw.normH*emw.normH (m*A^2), and
        // emw.normH*emw.normH (A^2/m^2)
        // It is important that the order is the same as it is written here
        String[][] tableData = model.result().table("tbl1").getTableData(false);
        // Print all retrieved data to ensure it is correct
        System.out.println("Table Data:");
        for (int i = 0; i < tableData.length; i++) {
            System.out.println(Arrays.toString(tableData[i]));
        }

        // Convert require values in "tbl1" to doubles
        double qFactor = Double.parseDouble(tableData[0][2]);
        double vmNumerator = Double.parseDouble(tableData[0][3]);
        double vmDenominator = Double.parseDouble(tableData[0][4]);

        // Compute initial Purcell factor (Fp)
        double purcellFactor = (qFactor * vmDenominator) / vmNumerator;
        // Print initial Fp ensure it is correct
        System.out.println("init Fp: " + purcellFactor);
    }

    public static void logGeneration(
            EvolutionResult<BitGene, Double> result,
            List<Integer> subgridDomains) {

        int currentGeneration = (int) result.generation(); // Get current generation number
        BitChromosome bestChromosome = (BitChromosome) result.bestPhenotype().genotype().get(0);

        // Get best Purcell Factor directly
        double bestPurcellFactor = result.bestFitness();

        // Compute the average Purcell Factor across the population
        double averagePurcellFactor = result.population().stream()
                .mapToDouble(ind -> ind.fitness()) // Extract fitness values
                .average()
                .orElse(0.0); // Default to 0 if empty

        DomainAssignment assignment = DomainUtils.extractDomainAssignments(
                bestChromosome,
                subgridDomains);

        // Log to console
        System.out.println("Generation: " + currentGeneration + " | Best Purcell Factor: " + bestPurcellFactor);
        System.out.println("Domains assigned to matDielec: " + assignment.dielectricDomains);
        System.out.println("Domains assigned to matAir: " + assignment.airDomains);

        CsvManager.appendGenerationToCsv(
                currentGeneration,
                bestPurcellFactor,
                averagePurcellFactor,
                assignment);
    }
}
