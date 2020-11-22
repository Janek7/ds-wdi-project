package de.uni_mannheim.informatik.web_data_integration.blocking;

public class ExecutionResult {

    private double f1;
    private double reductionRatio;

    public ExecutionResult(double f1, double reductionRatio) {
        this.f1 = f1;
        this.reductionRatio = reductionRatio;
    }

    public double getF1() {
        return f1;
    }

    public double getReductionRatio() {
        return reductionRatio;
    }
}
