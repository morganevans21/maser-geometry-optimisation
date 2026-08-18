package main.java.simulation;

import com.comsol.model.Model;

public class StudyRunner {

    public static void runStudy(Model model) {

        try {
            model.study("std1").run(); // Solve the study
            System.out.println("Study solved successfully.");
        } catch (Exception e) {
            System.err.println("Error during study run: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateNumericalResults(Model model) {

        // Ensure numerical results are updated
        model.result().numerical("gev1").setResult();
        model.result().numerical("int1").setResult();
        model.result().numerical("max1").setResult();

    }
}
