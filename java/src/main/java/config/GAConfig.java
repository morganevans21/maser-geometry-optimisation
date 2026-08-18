package main.java.config;

public final class GAConfig {

    public final int populationSize;
    public final int generations;
    public final double mutationRate;
    public final double crossoverRate;
    public final double targetFrequency;

    private GAConfig(
            int populationSize,
            int generations,
            double mutationRate,
            double crossoverRate,
            double targetFrequency) {

        this.populationSize = populationSize;
        this.generations = generations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.targetFrequency = targetFrequency;
    }

    public static GAConfig createGAConfig() {

        return new GAConfig(
                20,
                250,
                0.05,
                0.8,
                1.45e9);
    }
}
