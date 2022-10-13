package utils.predicates;

import utils.Particle;
import utils.missions.AbstractMission;

import java.math.BigDecimal;

public class EnteredOrbitPredicate extends Predicate {
    private State state;
    private boolean enteredOrbit;
    private final Particle sun;
    private final AbstractMission.MissionTarget missionTarget;

    public EnteredOrbitPredicate(Particle sun, AbstractMission.MissionTarget missionTarget) {
        this.enteredOrbit = false;
        this.state = State.MISSED;
        this.sun = sun;
        this.missionTarget = missionTarget;
    }

    @Override
    public boolean predict(Particle spaceship, Particle target) {
        if(spaceship == null) return false;
        double dist = spaceship.distance(target);

        if (missionTarget == AbstractMission.MissionTarget.VENUS) {
            return predictVenus(spaceship, target, dist);
        }
        return predictMars(target, dist);
    }

    private boolean predictVenus(Particle spaceship, Particle target, double dist) {
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

    private boolean predictMars(Particle target, double dist) {
        int DEIMOS = 23460;
        if(dist <= target.getRadius() + DEIMOS) {
            state = State.IN_ORBIT;
            if(!enteredOrbit){
                enteredOrbit = true;
            }
            if(dist <= target.getRadius()) { // LANDED
                state = State.LANDED;
                return true;
            }
        } else {
            if(enteredOrbit) {
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
        System.out.println(state);
    }
}
