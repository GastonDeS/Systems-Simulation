package utils.algorithms;

import utils.Particle;
import utils.UpdateMethod;


public class EulerAlgorithm extends AlgorithmImpl implements Algorithm {

    protected EulerAlgorithm(double K, double gamma, UpdateMethod updateMethod) {
        super(K, gamma, updateMethod);
    }
    public EulerAlgorithm(double K, double gamma) {
        super(K, gamma, UpdateMethod.EULER);
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
        double velX = isModified ? next.getVelX() : current.getVelX();
        double velY = isModified ? next.getVelY() : current.getVelY();
        next.setPosX(current.getPosX() + deltaT * velX + Math.pow(deltaT, 2) * current.getAccX()/2);
        next.setPosY(current.getPosY() + deltaT * velY + Math.pow(deltaT, 2) * current.getAccY()/2);
    }

    protected void updateVel(Particle current, Particle next, double deltaT) {
        next.setVelX(current.getVelX() + deltaT * current.getAccX());
        next.setVelY(current.getVelY() + deltaT * current.getAccY());
    }

    protected void updateForce(Particle next) {
        double mass = next.getMass();
        next.setAccX((-K * next.getPosX() - gamma * next.getVelX())/mass);
        next.setAccY((-K * next.getPosY() - gamma * next.getVelY())/mass);
    }
}
