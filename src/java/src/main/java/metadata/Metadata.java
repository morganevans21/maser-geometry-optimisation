package main.java.metadata;

import java.time.Instant;
import java.util.Map;

/**
 * Represents metadata for an optimization run.
 * This class holds all the information that should be stored in the metadata.json file.
 */
public class Metadata {
    private final String schemaVersion;
    private final Experiment experiment;
    private final Problem problem;
    private final Algorithm algorithm;
    private final Randomness randomness;
    private final Software software;
    private final Environment environment;
    private final Inputs inputs;
    private final Execution execution;
    private final Results results;

    public Metadata(String schemaVersion,
                    Experiment experiment,
                    Problem problem,
                    Algorithm algorithm,
                    Randomness randomness,
                    Software software,
                    Environment environment,
                    Inputs inputs,
                    Execution execution,
                    Results results) {
        this.schemaVersion = schemaVersion;
        this.experiment = experiment;
        this.problem = problem;
        this.algorithm = algorithm;
        this.randomness = randomness;
        this.software = software;
        this.environment = environment;
        this.inputs = inputs;
        this.execution = execution;
        this.results = results;
    }

    // Getters
    public String getSchemaVersion() { return schemaVersion; }
    public Experiment getExperiment() { return experiment; }
    public Problem getProblem() { return problem; }
    public Algorithm getAlgorithm() { return algorithm; }
    public Randomness getRandomness() { return randomness; }
    public Software getSoftware() { return software; }
    public Environment getEnvironment() { return environment; }
    public Inputs getInputs() { return inputs; }
    public Execution getExecution() { return execution; }
    public Results getResults() { return results; }

    /**
     * Returns a JSON string representation of this metadata.
     * @return JSON string
     */
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"schema_version\":").append(quote(schemaVersion)).append(",");
        json.append("\"experiment\":").append(experiment.toJson()).append(",");
        json.append("\"problem\":").append(problem.toJson()).append(",");
        json.append("\"algorithm\":").append(algorithm.toJson()).append(",");
        json.append("\"randomness\":").append(randomness.toJson()).append(",");
        json.append("\"software\":").append(software.toJson()).append(",");
        json.append("\"environment\":").append(environment.toJson()).append(",");
        json.append("\"inputs\":").append(inputs.toJson()).append(",");
        json.append("\"execution\":").append(execution.toJson()).append(",");
        json.append("\"results\":").append(results.toJson());
        json.append("}");
        return json.toString();
    }

    /**
     * Experiment metadata
     */
    public static class Experiment {
        private final String experimentId;
        private final String method;
        private final String description;

        public Experiment(String experimentId, String method, String description) {
            this.experimentId = experimentId;
            this.method = method;
            this.description = description;
        }

        public String getExperimentId() { return experimentId; }
        public String getMethod() { return method; }
        public String getDescription() { return description; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"experiment_id\":").append(quote(experimentId)).append(",");
            json.append("\"method\":").append(quote(method)).append(",");
            json.append("\"description\":").append(quote(description));
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Problem metadata
     */
    public static class Problem {
        private final String objective;
        private final long targetFrequencyHz;
        private final double lossTangent;

        public Problem(String objective, long targetFrequencyHz, double lossTangent) {
            this.objective = objective;
            this.targetFrequencyHz = targetFrequencyHz;
            this.lossTangent = lossTangent;
        }

        public String getObjective() { return objective; }
        public long getTargetFrequencyHz() { return targetFrequencyHz; }
        public double getLossTangent() { return lossTangent; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"objective\":").append(quote(objective)).append(",");
            json.append("\"target_frequency_hz\":").append(targetFrequencyHz).append(",");
            json.append("\"loss_tangent\":").append(lossTangent);
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Algorithm metadata - base class for algorithm-specific parameters
     */
    public static abstract class Algorithm {
        public abstract String toJson();
    }

    /**
     * Genetic Algorithm specific parameters
     */
    public static class GAAlgorithm extends Algorithm {
        private final int populationSize;
        private final int generations;
        private final double mutationProbability;
        private final double crossoverProbability;

        public GAAlgorithm(int populationSize, int generations, double mutationProbability, double crossoverProbability) {
            this.populationSize = populationSize;
            this.generations = generations;
            this.mutationProbability = mutationProbability;
            this.crossoverProbability = crossoverProbability;
        }

        public int getPopulationSize() { return populationSize; }
        public int getGenerations() { return generations; }
        public double getMutationProbability() { return mutationProbability; }
        public double getCrossoverProbability() { return crossoverProbability; }

        @Override
        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"population_size\":").append(populationSize).append(",");
            json.append("\"generations\":").append(generations).append(",");
            json.append("\"mutation_probability\":").append(mutationProbability).append(",");
            json.append("\"crossover_probability\":").append(crossoverProbability);
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Random Search specific parameters
     */
    public static class RandomSearchAlgorithm extends Algorithm {
        private final int populationSize;
        private final int generations;

        public RandomSearchAlgorithm(int populationSize, int generations) {
            this.populationSize = populationSize;
            this.generations = generations;
        }

        public int getPopulationSize() { return populationSize; }
        public int getGenerations() { return generations; }

        @Override
        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"population_size\":").append(populationSize).append(",");
            json.append("\"generations\":").append(generations);
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Hill Climbing specific parameters
     */
    public static class HillClimbAlgorithm extends Algorithm {
        private final int populationSize;
        private final int generations;

        public HillClimbAlgorithm(int populationSize, int generations) {
            this.populationSize = populationSize;
            this.generations = generations;
        }

        public int getPopulationSize() { return populationSize; }
        public int getGenerations() { return generations; }

        @Override
        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"population_size\":").append(populationSize).append(",");
            json.append("\"generations\":").append(generations);
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Randomness metadata
     */
    public static class Randomness {
        private final long seed;
        private final String generator;

        public Randomness(long seed, String generator) {
            this.seed = seed;
            this.generator = generator;
        }

        public long getSeed() { return seed; }
        public String getGenerator() { return generator; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"seed\":").append(seed).append(",");
            json.append("\"generator\":").append(quote(generator));
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Software metadata
     */
    public static class Software {
        private final Application application;
        private final String java;
        private final String comsol;
        private final String jenetics;

        public Software(Application application, String java, String comsol, String jenetics) {
            this.application = application;
            this.java = java;
            this.comsol = comsol;
            this.jenetics = jenetics;
        }

        public Application getApplication() { return application; }
        public String getJava() { return java; }
        public String getComsol() { return comsol; }
        public String getJenetics() { return jenetics; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"application\":").append(application.toJson()).append(",");
            json.append("\"java\":").append(quote(java)).append(",");
            json.append("\"comsol\":").append(quote(comsol)).append(",");
            json.append("\"jenetics\":").append(quote(jenetics));
            json.append("}");
            return json.toString();
        }

        /**
         * Application metadata
         */
        public static class Application {
            private final String name;
            private final String version;

            public Application(String name, String version) {
                this.name = name;
                this.version = version;
            }

            public String getName() { return name; }
            public String getVersion() { return version; }

            public String toJson() {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"name\":").append(quote(name)).append(",");
                json.append("\"version\":").append(quote(version));
                json.append("}");
                return json.toString();
            }
        }
    }

    /**
     * Environment metadata
     */
    public static class Environment {
        private final String os;
        private final String architecture;
        private final String cpu;
        private final double availableMemoryGb;

        public Environment(String os, String architecture, String cpu, double availableMemoryGb) {
            this.os = os;
            this.architecture = architecture;
            this.cpu = cpu;
            this.availableMemoryGb = availableMemoryGb;
        }

        public String getOs() { return os; }
        public String getArchitecture() { return architecture; }
        public String getCpu() { return cpu; }
        public double getAvailableMemoryGb() { return availableMemoryGb; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"os\":").append(quote(os)).append(",");
            json.append("\"architecture\":").append(quote(architecture)).append(",");
            json.append("\"cpu\":").append(quote(cpu)).append(",");
            json.append("\"available_memory_gb\":").append(availableMemoryGb);
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Inputs metadata
     */
    public static class Inputs {
        private final String model;
        private final String modelVersion;
        private final String dataset;

        public Inputs(String model, String modelVersion, String dataset) {
            this.model = model;
            this.modelVersion = modelVersion;
            this.dataset = dataset;
        }

        public String getModel() { return model; }
        public String getModelVersion() { return modelVersion; }
        public String getDataset() { return dataset; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"model\":").append(quote(model)).append(",");
            json.append("\"model_version\":").append(quote(modelVersion)).append(",");
            json.append("\"dataset\":").append(quote(dataset));
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Execution metadata
     */
    public static class Execution {
        private final String startedAt;
        private final String finishedAt;
        private final double runtimeSeconds;
        private final long femEvaluations;

        public Execution(String startedAt, String finishedAt, double runtimeSeconds, long femEvaluations) {
            this.startedAt = startedAt;
            this.finishedAt = finishedAt;
            this.runtimeSeconds = runtimeSeconds;
            this.femEvaluations = femEvaluations;
        }

        public String getStartedAt() { return startedAt; }
        public String getFinishedAt() { return finishedAt; }
        public double getRuntimeSeconds() { return runtimeSeconds; }
        public long getFemEvaluations() { return femEvaluations; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"started_at\":").append(quote(startedAt)).append(",");
            json.append("\"finished_at\":").append(quote(finishedAt)).append(",");
            json.append("\"runtime_seconds\":").append(runtimeSeconds).append(",");
            json.append("\"fem_evaluations\":").append(femEvaluations);
            json.append("}");
            return json.toString();
        }
    }

    /**
     * Results metadata
     */
    public static class Results {
        private final double bestFitness;
        private final Chromosome bestChromosome;

        public Results(double bestFitness, Chromosome bestChromosome) {
            this.bestFitness = bestFitness;
            this.bestChromosome = bestChromosome;
        }

        public double getBestFitness() { return bestFitness; }
        public Chromosome getBestChromosome() { return bestChromosome; }

        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"best_fitness\":").append(bestFitness).append(",");
            json.append("\"best_chromosome\":").append(bestChromosome.toJson());
            json.append("}");
            return json.toString();
        }

        /**
         * Chromosome metadata
         */
        public static class Chromosome {
            private final String representation;
            private final int length;
            private final String value;

            public Chromosome(String representation, int length, String value) {
                this.representation = representation;
                this.length = length;
                this.value = value;
            }

            public String getRepresentation() { return representation; }
            public int getLength() { return length; }
            public String getValue() { return value; }

            public String toJson() {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"representation\":").append(quote(representation)).append(",");
                json.append("\"length\":").append(length).append(",");
                json.append("\"value\":").append(quote(value));
                json.append("}");
                return json.toString();
            }
        }
    }

    /**
     * Builder pattern for constructing Metadata objects
     */
    public static class Builder {
        private String schemaVersion = "1.0";
        private Experiment experiment;
        private Problem problem;
        private Algorithm algorithm;
        private Randomness randomness;
        private Software software;
        private Environment environment;
        private Inputs inputs;
        private Execution execution;
        private Results results;

        public Builder setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
            return this;
        }

        public Builder setExperiment(Experiment experiment) {
            this.experiment = experiment;
            return this;
        }

        public Builder setProblem(Problem problem) {
            this.problem = problem;
            return this;
        }

        public Builder setAlgorithm(Algorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder setRandomness(Randomness randomness) {
            this.randomness = randomness;
            return this;
        }

        public Builder setSoftware(Software software) {
            this.software = software;
            return this;
        }

        public Builder setEnvironment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder setInputs(Inputs inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder setExecution(Execution execution) {
            this.execution = execution;
            return this;
        }

        public Builder setResults(Results results) {
            this.results = results;
            return this;
        }

        public Metadata build() {
            // Validate required fields
            if (experiment == null) throw new IllegalStateException("Experiment is required");
            if (problem == null) throw new IllegalStateException("Problem is required");
            if (algorithm == null) throw new IllegalStateException("Algorithm is required");
            if (randomness == null) throw new IllegalStateException("Randomness is required");
            if (software == null) throw new IllegalStateException("Software is required");
            if (environment == null) throw new IllegalStateException("Environment is required");
            if (inputs == null) throw new IllegalStateException("Inputs are required");
            if (execution == null) throw new IllegalStateException("Execution is required");
            if (results == null) throw new IllegalStateException("Results are required");

            return new Metadata(schemaVersion, experiment, problem, algorithm, randomness, software, environment, inputs, execution, results);
        }
    }

    /**
     * Escapes a string for JSON.
     * @param s the string to escape
     * @return the escaped string wrapped in double quotes
     */
    private static String quote(String s) {
        if (s == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('\"');
        return sb.toString();
    }
}