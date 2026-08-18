package main.java.model;

public final class SimulationResult {

    public final double frequency;
    public final double qFactor;
    public final double vmNumerator;
    public final double vmDenominator;
    public final double purcellFactor;

    public SimulationResult(
            double frequency,
            double qFactor,
            double vmNumerator,
            double vmDenominator) {

        this.frequency = frequency;
        this.qFactor = qFactor;
        this.vmNumerator = vmNumerator;
        this.vmDenominator = vmDenominator;
        this.purcellFactor = (qFactor * vmDenominator) / vmNumerator;
    }
}
