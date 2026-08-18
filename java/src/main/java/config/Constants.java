package main.java.config;

import java.nio.file.Path;

public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final Path RESULTS_DIRECTORY = Path.of("..", "results", "raw");

    public static final Path RESULTS_FILE = RESULTS_DIRECTORY.resolve("ga-results.csv");

    public static final Path LOG_FILE = RESULTS_DIRECTORY.resolve("ga-logs.csv");
}
