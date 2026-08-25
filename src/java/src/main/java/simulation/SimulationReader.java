package main.java.simulation;

import com.comsol.model.Model;

import main.java.model.SimulationResult;

public class SimulationReader {

    public static SimulationResult readSimulationResults(Model model) {

        // Retrieve numerical values
        double frequency = model.result().numerical("gev1").getReal()[0][0]; // First row, first column
        double qFactor = model.result().numerical("gev1").getReal()[1][0];
        double vmNumerator = model.result().numerical("int1").getReal()[0][0];
        double vmDenominator = model.result().numerical("max1").getReal()[0][0];

        return new SimulationResult(
                frequency,
                qFactor,
                vmNumerator,
                vmDenominator);
    }
}
