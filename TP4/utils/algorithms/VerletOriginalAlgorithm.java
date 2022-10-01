package utils.algorithms;

import utils.Particle;

public class VerletOriginalAlgorithm implements Algorithm {
    private final double k;
    private final double gamma;

    public VerletOriginalAlgorithm(double k, double gamma) {
        this.k = k;
        this.gamma = gamma;
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT) {
        if (current == null) throw new NullPointerException("No current particle to update with");
        double r = current.getPosX();
        double v = current.getVelX();
        double f = -k * r - gamma * v;
        double rNext = 2*r - previous.getPosX() + Math.pow(deltaT, 2) * f;
        double vNext = (rNext - previous.getPosX()) / (2 * deltaT);
        double aNext = (-k * rNext - gamma * vNext) / current.getMass();
        return new Particle('-', current.getMass(), current.getRadius(), rNext, 0, vNext, 0, aNext, 0);
    }
}
