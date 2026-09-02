package main.java.ga;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.comsol.model.Model;

import main.java.config.GAConfig;
import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.EvolutionResult;
import main.java.config.GAConfig;
import main.java.io.CsvManager;
import main.java.metadata.Metadata;
import main.java.metadata.MetadataService;
import main.java.simulation.ModelInitialiser;
import main.java.simulation.StudyRunner;
import main.java.util.DomainUtils;

/**
 * Hill Climbing baseline: iteratively improves solution by accepting
 * only fitness-improving moves in the neighborhood.
 * Uses identical evaluation pipeline and computational budget as GA.
 */
public class HillClimbing {

    /**
     * Performs hill climbing search and returns the best solution found.
     * This method mirrors the interface of GeneticAlgorithm.runGeneticAlgorithm().
     *
     * @param fitnessFunction Function to evaluate candidate solutions
     * @param subgridDomains List of domain IDs for the subgrid
     * @param config Configuration containing population size, generations, and seed
     * @return EvolutionResult containing the best solution found
     */
    public static EvolutionResult<BitGene, Double> runHillClimbing(
            Function<Genotype<BitGene>, Double> fitnessFunction,
            List<Integer> subgridDomains,
            GAConfig config) {

        // Initialize random number generator with seed for reproducibility
        Random random = new Random(config.seed);

        // Start with random solution (same initialization as Random Search)
        BitChromosome current = BitChromosome.of(100);
        for (int i = 0; i < 100; i++) {
            current.set(i, random.nextBoolean());
        }
        Genotype<BitGene> currentGenotype = Genotype.of(current);
        double currentFitness = fitnessFunction.apply(currentGenotype);

        // Track best solution ever seen
        double bestFitness = currentFitness;
        Genotype<BitGene> bestGenotype = currentGenotype;

        // Evaluation budget: identical to GA and Random Search
        int totalEvaluations = config.populationSize * config.generations;
        int evaluationsUsed = 1; // Already used 1 for initial solution

        // Perform hill climbing
        while (evaluationsUsed < totalEvaluations) {
            // Generate neighbor: flip one random bit (Hamming distance 1)
            BitChromosome neighbor = new BitChromosome(current);
            int flipIndex = random.nextInt(100);
            neighbor.flip(flipIndex);

            Genotype<BitGene> neighborGenotype = Genotype.of(neighbor);
            double neighborFitness = fitnessFunction.apply(neighborGenotype);
            evaluationsUsed++;

            // Hill Climbing rule: ONLY move if strictly better
            if (neighborFitness > currentFitness) {
                current = neighbor;
                currentGenotype = neighborGenotype;
                currentFitness = neighborFitness;

                // Update global best if improved
                if (currentFitness > bestFitness) {
                    bestFitness = currentFitness;
                    bestGenotype = currentGenotype;
                }
            }
            // If not better, stay at current solution (no move)
        }

        // Return result in same format as Jenetics EvolutionResult
        // We need to create a mock EvolutionResult since we're not using Jenetics engine
        return new EvolutionResult<BitGene, Double>() {
            @Override
            public Genotype<BitGene> getBestGenotype() {
                return bestGenotype;
            }

            @Override
            public EvolutionResult.Phenotype<BitGene, Double> bestPhenotype() {
                return () -> bestGenotype;
            }

            @Override
            public double getBestFitness() {
                return bestFitness;
            }

            @Override
            public int getGenerations() {
                // For hill climbing, we don't have generations, but return 1 to indicate completion
                return 1;
            }

            @Override
            public long getOffspringCount() {
                return totalEvaluations;
            }

            @Override
            public long getYoungestAge() {
                return 0;
            }
        };
    }

    /**
     * Applies the best solution found by hill climbing to the COMSOL model.
     * Mirrors the functionality in GeneticAlgorithm.applyBestSolution().
     *
     * @param model COMSOL model to apply solution to
     * @param result EvolutionResult containing the best solution
     * @param subgridDomains List of domain IDs for the subgrid
     */
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

    /**
     * Main method for running Hill Climbing independently.
     *
     * @param args Command line arguments: [seed]
     *             If not provided, defaults to seed 12345
     */
    public static void main(String[] args) {
        long seed = 12345L; // Default seed for reproducibility

        // Parse command line arguments
        if (args.length > 0) {
            try {
                seed = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid seed value: " + args[0] + ". Using default seed: " + seed);
            }
        }

        runComsolHillClimbing(seed);
    }

    /**
     * Runs hill climbing using the COMSOL objective function.
     * This preserves the original functionality for research runs.
     *
     * @param seed random seed for reproducibility
     */
    private static void runComsolHillClimbing(long seed) {
        // Record start time
        Instant startTime = Instant.now();

        Model model = ModelInitialiser.initialiseModel();
        ModelInitialiser.printInitialResults(model);
        List<Integer> subgridDomains = DomainUtils.createSubgridDomains();

        GAConfig config = GAConfig.createGAConfigWithSeed(seed);

        // Initialize run directory and reset FEM counter
        File runDir = null;
        try {
            runDir = CsvManager.initializeRunDirectory(config);
            StudyRunner.resetFemEvaluationCounter();
        } catch (IOException e) {
            System.err.println("Failed to initialize run directory: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Define fitness function for hill climbing (maximize Purcell factor)
        Function<Genotype<BitGene>, Double> fitnessFunction = FitnessFunctionFactory.createFitnessFunction(model, subgridDomains, config);

        // Run hill climbing
        EvolutionResult<BitGene, Double> result = HillClimbing.runHillClimbing(
                fitnessFunction,
                subgridDomains,
                config);

        // Best solution
        HillClimbing.applyBestSolution(model, result, subgridDomains);

        // Record end time
        Instant endTime = Instant.now();
        double runtimeSeconds = java.time.Duration.between(startTime, endTime).getSeconds() +
                java.time.Duration.between(startTime, endTime).getNano() / 1_000_000_000.0;

        // Prepare metadata
        Metadata metadata = createMetadata(
                startTime, endTime, runtimeSeconds, config, result, subgridDomains, runDir);

        // Write metadata to file
        try {
            MetadataService.writeMetadata(metadata, runDir);
        } catch (IOException e) {
            System.err.println("Failed to write metadata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a metadata object for the optimization run.
     *
     * @param startTime the start time of the run
     * @param endTime the end time of the run
     * @param runtimeSeconds the runtime in seconds
     * @param config the GA configuration
     * @param result the evolution result
     * @param subgridDomains list of subgrid domain IDs
     * @param runDir the run directory
     * @return the metadata object
     */
    private static Metadata createMetadata(
            Instant startTime, Instant endTime, double runtimeSeconds,
            GAConfig config, EvolutionResult<BitGene, Double> result,
            List<Integer> subgridDomains, File runDir) {

        // Experiment metadata
        Metadata.Experiment experiment = new Metadata.Experiment(
                "hill_climb_lossless_seed_" + config.seed,
                "hill_climb",
                "Hill Climb optimization of Purcell factor");

        // Problem metadata
        Metadata.Problem problem = new Metadata.Problem(
                "purcell_factor",
                Math.round(config.targetFrequency), // Convert to Hz and round to long
                0.0); // Loss tangent is 0.0 (assumed)

        // Algorithm metadata (Hill Climbing-specific)
        Metadata.Algorithm algorithm = new Metadata.HillClimbAlgorithm(
                config.populationSize,
                config.generations);

        // Randomness metadata
        Metadata.Randomness randomness = new Metadata.Randomness(
                config.seed,
                "java.util.Random");

        // Software metadata
        Metadata.Software software = new Metadata.Software(
                new Metadata.Software.Application(
                        SoftwareInfo.getApplicationName(),
                        SoftwareInfo.getApplicationVersion()),
                SoftwareInfo.getJavaVersion(),
                SoftwareInfo.getComsolVersion(),
                SoftwareInfo.getJeneticsVersion());

        // Environment metadata
        Metadata.Environment environment = new Metadata.Environment(
                EnvironmentInfo.getOs(),
                EnvironmentInfo.getArchitecture(),
                EnvironmentInfo.getCpu(),
                EnvironmentInfo.getAvailableMemoryGb());

        // Inputs metadata
        Metadata.Inputs inputs = new Metadata.Inputs(
                "ComsolModel.mph", // Model name
                null, // Model version unknown
                null); // Dataset unknown

        // Execution metadata
        Metadata.Execution execution = new Metadata.Execution(
                formatInstant(startTime),
                formatInstant(endTime),
                runtimeSeconds,
                StudyRunner.getFemEvaluationCount());

        // Results metadata
        // Convert best chromosome to binary string
        BitChromosome bestChromosome = (BitChromosome) result.bestPhenotype().genotype().get(0);
        StringBuilder chromosomeValue = new StringBuilder();
        for (int i = 0; i < bestChromosome.length(); i++) {
            chromosomeValue.append(bestChromosome.get(i).booleanValue() ? '1' : '0');
        }
        Metadata.Results.ResultsBuilder resultsBuilder = new Metadata.Results.ResultsBuilder();
        Metadata.Results results = resultsBuilder
                .setBestFitness(result.bestFitness())
                .setBestChromosome(new Metadata.Results.Chromosome(
                        "binary",
                        bestChromosome.length(),
                        chromosomeValue.toString()))
                .build();

        // Build and return metadata
        return new Metadata.Builder()
                .setSchemaVersion("1.0")
                .setExperiment(experiment)
                .setProblem(problem)
                .setAlgorithm(algorithm)
                .setRandomness(randomness)
                .setSoftware(software)
                .setEnvironment(environment)
                .setInputs(inputs)
                .setExecution(execution)
                .setResults(results)
                .build();
    }

    /**
     * Formats an Instant to an ISO 8601 string.
     *
     * @param instant the instant to format
     * @return the formatted string
     */
    private static String formatInstant(Instant instant) {
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }
}