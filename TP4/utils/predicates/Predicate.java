package utils.predicates;

import utils.Particle;

public abstract class Predicate {
    public abstract boolean predict(Particle spaceship);

    public abstract State getState();

    public abstract void print();

    public enum State {
        LANDED, IN_ORBIT, MISSED
    }
}
