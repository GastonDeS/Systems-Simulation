package utils.predicates;

import utils.Particle;

public class EnteredOrbitPredicate extends Predicate {
    private State state;
    private boolean enteredOrbit;

    public EnteredOrbitPredicate() {
        this.enteredOrbit = false;
        this.state = State.MISSED;
    }

    @Override
    public boolean predict(Particle spaceship, Particle target) {
        if(spaceship == null) return false;
        double dist = spaceship.distance(target);
        if(dist <= target.getRadius()) { // todo: add limit
            enteredOrbit = true;
            if(state == State.MISSED) {
                state = State.IN_ORBIT; // when added limit
            }
            if(dist <= target.getRadius()) {
                state = State.LANDED;
                return true;
            }
        } else {
            if(enteredOrbit) {
                enteredOrbit = false;
                state = State.MISSED;
                return true;
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

    }
}
