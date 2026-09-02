package main.java.metadata;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * Provides environment information.
 */
public class EnvironmentInfo {

    /**
     * Gets the operating system name.
     *
     * @return the operating system name
     */
    public static String getOs() {
        return System.getProperty("os.name");
    }

    /**
     * Gets the operating system architecture.
     *
     * @return the operating system architecture
     */
    public static String getArchitecture() {
        return System.getProperty("os.arch");
    }

    /**
     * Gets the CPU information.
     * Note: Getting the exact CPU model is platform-dependent and not straightforward in Java.
     * We return the number of available processors as a string for now.
     *
     * @return a string representing the CPU (number of processors)
     */
    public static String getCpu() {
        int processors = Runtime.getRuntime().availableProcessors();
        return processors + " processor(s)";
    }

    /**
     * Gets the available memory in GB.
     *
     * @return the available memory in GB
     */
    public static double getAvailableMemoryGb() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        return maxMemory / (1024.0 * 1024.0 * 1024.0);
    }
}