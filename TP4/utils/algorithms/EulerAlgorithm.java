package utils.algorithms;

import utils.Particle;

public class EulerAlgorithm implements Algorithm {
    private final double K;
    private final double gamma;

    public EulerAlgorithm(double K, double gamma) {
        this.K = K;
        this.gamma = gamma;
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT) {
        if (current == null) throw new NullPointerException("No current particle to update with");
        Particle next = current.clone();
        updatePos(current, next, deltaT, false);
        updateVel(current, next, deltaT);
        updateForce(next);
        return next;
    }

    protected void updatePos(Particle current, Particle next, double deltaT, boolean isModified) {
        double mass = current.getMass();
        double velX = isModified ? next.getVelX() : current.getVelX();
        double velY = isModified ? next.getVelY() : current.getVelY();
        next.setPosX(current.getPosX() + deltaT * velX + Math.pow(deltaT, 2) * current.getForceX()/(2 * mass));
        next.setPosY(current.getPosY() + deltaT * velY + Math.pow(deltaT, 2) * current.getForceY()/(2 * mass));
    }

    protected void updateVel(Particle current, Particle next, double deltaT) {
        double mass = current.getMass();
        next.setVelX(current.getVelX() + deltaT * current.getForceX()/mass);
        next.setVelY(current.getVelY() + deltaT * current.getForceY()/mass);
    }

    protected void updateForce(Particle next) {
        next.setForceX(-K * next.getPosX() - gamma * next.getVelX());
        next.setForceY(-K * next.getPosY() - gamma * next.getVelY());
    }
}
