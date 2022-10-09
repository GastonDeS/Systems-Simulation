package utils;

public class Config {
    private double deltaT;
    private double maxTime;
    private int steps;
    private double takeOffTime;

    public Config() {}

    public Config withDeltaT(double deltaT) {
        this.deltaT = deltaT;
        return this;
    }

    public Config withMaxTime(double maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public Config withSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public Config withTakeOffTime(double takeOffTime) {
        this.takeOffTime = takeOffTime;
        return this;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public int getSteps() {
        return steps;
    }

    public double getTakeOffTime() {
        return takeOffTime;
    }
}
