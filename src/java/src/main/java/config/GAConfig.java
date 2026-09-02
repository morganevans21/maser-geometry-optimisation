package main.java.config;

public final class GAConfig {

    public final int populationSize;
    public final int generations;
    public final double mutationRate;
    public final double crossoverRate;
    public final double targetFrequency;
    public final long seed;

    private GAConfig(
            int populationSize,
            int generations,
            double mutationRate,
            double crossoverRate,
            double targetFrequency,
            long seed) {

        this.populationSize = populationSize;
        this.generations = generations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.targetFrequency = targetFrequency;
        this.seed = seed;
    }

    public static GAConfig createGAConfig() {
        return createGAConfigWithSeed(0); // Default seed of 0 for backward compatibility
    }

    public static GAConfig createGAConfigWithSeed(long seed) {
        return new GAConfig(
                20,
                250,
                0.05,
                0.8,
                1.45e9,
                seed);
    }
}
