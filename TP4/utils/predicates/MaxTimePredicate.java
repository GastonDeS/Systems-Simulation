package utils.predicates;

import utils.Particle;

public class MaxTimePredicate extends Predicate {
    private double time;
    private final double maxTime;
    private final double deltaT;

    public MaxTimePredicate(double maxTime, double deltaT) {
        this.maxTime = maxTime;
        this.deltaT = deltaT;
        this.time = 0;
    }

    @Override
    public boolean predict(Particle spaceship) {
        time += deltaT;
        return time >= maxTime;
    }

    @Override
    public State getState() {
        return State.MISSED;
    }

    @Override
    public void print() {
        System.out.println("REACHED MAXIMUM TIME FOR SIMULATION\nTERMINATING...");
    }
}
