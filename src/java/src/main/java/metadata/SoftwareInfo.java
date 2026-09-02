package main.java.metadata;

/**
 * Provides software version information.
 */
public class SoftwareInfo {

    /**
     * Gets the Java version from system properties.
     *
     * @return the Java version
     */
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Gets the COMSOL version.
     * This is hardcoded based on the project's pom.xml.
     *
     * @return the COMSOL version
     */
    public static String getComsolVersion() {
        return "6.0.0.405";
    }

    /**
     * Gets the Jenetics version.
     * This is hardcoded based on the project's pom.xml.
     *
     * @return the Jenetics version
     */
    public static String getJeneticsVersion() {
        return "5.0.0";
    }

    /**
     * Gets the application name.
     *
     * @return the application name
     */
    public static String getApplicationName() {
        return "MaserGeometryOptimisation";
    }

    /**
     * Gets the application version.
     * This could be read from a version file or pom.xml, but for now we hardcode.
     *
     * @return the application version
     */
    public static String getApplicationVersion() {
        return "1.0.0";
    }
}