package utils.predicates;

import utils.Particle;
import utils.missions.AbstractMission;

import java.math.BigDecimal;

public class EnteredOrbitPredicate extends Predicate {
    private State state;
    private boolean enteredOrbit;
    private final Particle sun;

    public EnteredOrbitPredicate(Particle sun) {
        this.enteredOrbit = false;
        this.state = State.MISSED;
        this.sun = sun;
    }

    @Override
    public boolean predict(Particle spaceship, Particle target) {
        if(spaceship == null) return false;
        double dist = spaceship.distance(target);
        if(dist <= target.getRadius()) { // landed
            state = State.LANDED;
            return true;
        } else {
            if (BigDecimal.valueOf(target.distance(sun)).equals(BigDecimal.valueOf(spaceship.distance(sun)))) {
                state = State.IN_ORBIT;
                if (enteredOrbit) {
                    return true;
                } else {
                    enteredOrbit = true;
                }
            }
        }
        return false;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void print() {
        System.out.println(state);
    }
}
