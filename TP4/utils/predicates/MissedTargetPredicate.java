package utils.predicates;

import utils.Particle;

public class MissedTargetPredicate extends Predicate {
    private final double targetRadius;
    private final double targetOrbitRadius;

    public MissedTargetPredicate(Particle target) {
        this.targetRadius = target.getRadius();
        this.targetOrbitRadius = Math.sqrt(Math.pow(target.getPosX(), 2) + Math.pow(target.getPosY(), 2));
    }

    @Override
    public boolean predict(Particle spaceship) {
        if (spaceship == null) return false;
        double dist = Math.sqrt(Math.pow(spaceship.getPosX(), 2) + Math.pow(spaceship.getPosY(), 2));
        return dist < targetOrbitRadius - targetRadius;
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
