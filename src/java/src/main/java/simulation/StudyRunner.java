package main.java.simulation;

import com.comsol.model.Model;

/**
 * Runs the COMSOL study and tracks the number of FEM evaluations.
 */
public class StudyRunner {

    /** Counter for the number of FEM evaluations (study runs). */
    private static long femEvaluationCounter = 0;

    /**
     * Runs the study and increments the FEM evaluation counter.
     *
     * @param model The COMSOL model to run
     */
    public static void runStudy(Model model) {
        try {
            model.study("std1").run(); // Solve the study
            System.out.println("Study solved successfully.");
        } catch (Exception e) {
            System.err.println("Error during study run: " + e.getMessage());
            e.printStackTrace();
        } finally {
            femEvaluationCounter++;
        }
    }

    public static void updateNumericalResults(Model model) {

        // Ensure numerical results are updated
        model.result().numerical("gev1").setResult();
        model.result().numerical("int1").setResult();
        model.result().numerical("max1").setResult();

    }

    /**
     * Resets the FEM evaluation counter to zero.
     * This should be called at the start of each optimization run.
     */
    public static void resetFemEvaluationCounter() {
        femEvaluationCounter = 0;
    }

    /**
     * Gets the current FEM evaluation count.
     *
     * @return the number of times the study has been run
     */
    public static long getFemEvaluationCount() {
        return femEvaluationCounter;
    }
}