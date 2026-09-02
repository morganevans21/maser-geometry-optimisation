package main.java.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.comsol.model.Model;

import main.java.config.GAConfig;
import main.java.ga.GenerationLogger;
import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.SinglePointCrossover;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import main.java.simulation.MaterialManager;
import main.java.simulation.StudyRunner;

public class GeneticAlgorithm {

    public static Engine<BitGene, Double> createEngine(
            Function<Genotype<BitGene>, Double> fitnessFunction,
            GAConfig config) {

        return Engine.builder(fitnessFunction, BitChromosome.of(100))
                .populationSize(config.populationSize)
                .selector(new TournamentSelector<>(3)) // Selection strategy this could be changed to
                                                       // TournamentSelector(int) or EliteSelector(int)
                .alterers(new Mutator<>(config.mutationRate), new SinglePointCrossover<>(config.crossoverRate))
                .offspringFraction(0.8)
                .survivorsSelector(new EliteSelector<>(2))
                .random(java.util.Random::new, config.seed) // Initialize random number generator with seed
                .build();
    }

    public static EvolutionResult<BitGene, Double> runGeneticAlgorithm(
            Engine<BitGene, Double> engine,
            List<Integer> subgridDomains,
            GAConfig config) {

        return engine.stream()
                .limit(config.generations)
                .peek(result -> GenerationLogger.logGeneration(result, subgridDomains, config.seed))
                .collect(EvolutionResult.toBestEvolutionResult());
    }

    public static void applyBestSolution(
            Model model,
            EvolutionResult<BitGene, Double> result,
            List<Integer> subgridDomains) {

        BitChromosome bestChromosome = (BitChromosome) result.bestPhenotype().genotype().get(0);

        // Lists to store domain assignments
        List<Integer> dielectricDomains = new ArrayList<>();
        List<Integer> airDomains = new ArrayList<>();

        for (int i = 0; i < bestChromosome.length(); i++) {
            int domainID = subgridDomains.get(i); // Get correct domain ID

            boolean air = bestChromosome.get(i).booleanValue();

            MaterialManager.assignMaterial(
                    model,
                    domainID,
                    air);

            if (air) {
                airDomains.add(domainID);
            } else {
                dielectricDomains.add(domainID);
            }
        }

        // Print final domain assignments
        System.out.println("Domains assigned to matDielec: " + dielectricDomains);
        System.out.println("Domains assigned to matAir: " + airDomains);

        // Final run
        StudyRunner.runStudy(model);
    }
}
