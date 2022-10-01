package utils.algorithms;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
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
        updatePos(current, next, deltaT);
        updateVel(current, next, deltaT);
        updateForce(next);
        return next;
    }

    private void updatePos(Particle current, Particle next, double deltaT) {
        double mass = current.getMass();
        next.setPosX(current.getPosX() + deltaT * current.getVelX() + Math.pow(deltaT, 2) * current.getForceX()/(2 * mass));
        next.setPosY(current.getPosY() + deltaT * current.getVelY() + Math.pow(deltaT, 2) * current.getForceY()/(2 * mass));
    }

    private void updateVel(Particle current, Particle next, double deltaT) {
        double mass = current.getMass();
        next.setVelX(current.getVelX() + deltaT * current.getForceX()/mass);
        next.setVelY(current.getVelY() + deltaT * current.getForceY()/mass);
    }

    private void updateForce(Particle next) {
        next.setForceX(-K * next.getPosX() - gamma * next.getVelX());
        next.setForceY(-K * next.getPosY() - gamma * next.getVelY());
    }
}
