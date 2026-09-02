package main.java.config;

import java.io.File;
import java.nio.file.Path;

/**
 * Constants for the project.
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final Path RESULTS_BASE_DIRECTORY = Path.of("..", "results", "raw");

    /**
     * Gets the base results directory as a File.
     *
     * @return the base results directory
     */
    public static File getResultsBaseDirectory() {
        return RESULTS_BASE_DIRECTORY.toFile();
    }
}