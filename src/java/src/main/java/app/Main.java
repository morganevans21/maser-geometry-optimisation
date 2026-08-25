package main.java.app;

import java.util.List;
import java.util.function.Function;

import com.comsol.model.Model;

import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;

import main.java.config.GAConfig;
import main.java.ga.FitnessFunctionFactory;
import main.java.ga.GeneticAlgorithm;
import main.java.io.CsvManager;
import main.java.simulation.ModelInitialiser;
import main.java.util.DomainUtils;

public class Main {
    public static void main(String[] args) {

        Model model = ModelInitialiser.initialiseModel();

        ModelInitialiser.printInitialResults(model);

        List<Integer> subgridDomains = DomainUtils.createSubgridDomains();

        GAConfig config = GAConfig.createGAConfig();

        CsvManager.initialiseCsvFiles(config);

        // Define fitness function for the genetic algorithm (maximize Purcell factor)
        // This fitness function aims to maximise the Purcell factor
        // It evaluates good a particular material configuration is
        // The NxN subgrid is modelled as a binary matrix where `1` represents the
        // dielectric material and `0` represents air
        // Using this chromosome representation the fitness function then:
        // 1. Assigns each domain in subgrid a material
        // 2. Runs the COMSOL model
        // 3. Calculates the Purcell factor
        // 4. Iteratively tunes to the target frequency (1.45 GHz)
        // 5. Recalculates the Purcell factor and returns it as the fitness score

        Function<Genotype<BitGene>, Double> fitnessFunction = FitnessFunctionFactory.createFitnessFunction(model,
                subgridDomains, config);

        // Configure genetic algorithm
        // 10x10 grid represented as 100-bit chromosome, Add ', 0.5' for 50% random
        // initialisation
        Engine<BitGene, Double> engine = GeneticAlgorithm.createEngine(fitnessFunction, config);

        // Run evolution
        EvolutionResult<BitGene, Double> result = GeneticAlgorithm.runGeneticAlgorithm(engine, subgridDomains, config);

        // Best solution
        GeneticAlgorithm.applyBestSolution(model, result, subgridDomains);
    }
}
