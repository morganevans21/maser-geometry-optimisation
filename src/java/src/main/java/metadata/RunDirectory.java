package main.java.metadata;

import java.io.File;

/**
 * Holds the current run directory for the optimization.
 */
public class RunDirectory {
    private static File currentRunDirectory;

    private RunDirectory() {
        // Prevent instantiation
    }

    public static void setCurrentRunDirectory(File directory) {
        currentRunDirectory = directory;
    }

    public static File getCurrentRunDirectory() {
        return currentRunDirectory;
    }
}