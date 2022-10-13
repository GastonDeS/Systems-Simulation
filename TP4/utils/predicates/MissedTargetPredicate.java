package utils.predicates;

import utils.Particle;
import utils.missions.AbstractMission;

public class MissedTargetPredicate extends Predicate {
    private final double targetRadius;
    private final double targetOrbitRadius;
    private final AbstractMission.MissionTarget missionTarget;

    public MissedTargetPredicate(Particle target, AbstractMission.MissionTarget missionTarget) {
        this.targetRadius = target.getRadius();
        this.targetOrbitRadius = Math.sqrt(Math.pow(target.getPosX(), 2) + Math.pow(target.getPosY(), 2));
        this.missionTarget = missionTarget;
    }

    @Override
    public boolean predict(Particle spaceship, Particle target) {
        if (spaceship == null) return false;
        double dist = Math.sqrt(Math.pow(spaceship.getPosX(), 2) + Math.pow(spaceship.getPosY(), 2));
        if (missionTarget == AbstractMission.MissionTarget.VENUS) return dist < targetOrbitRadius - targetRadius;
        return dist > targetOrbitRadius + targetRadius;
    }

    @Override
    public State getState() {
        return State.MISSED;
    }

    @Override
    public void print() {
        System.out.println("SPACESHIP MISSED THE TARGET\nTERMINATING...");
    }
}
