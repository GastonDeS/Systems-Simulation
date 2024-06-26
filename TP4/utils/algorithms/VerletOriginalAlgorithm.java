package utils.algorithms;

import utils.Particle;

public class VerletOriginalAlgorithm implements Algorithm {
    private final Algorithm euler;
    private final double K;
    private final double gamma;

    public VerletOriginalAlgorithm(Algorithm euler, double K, double gamma) {
        this.euler = euler;
        this.K = K;
        this.gamma = gamma;
    }

    @Override
    public Particle update(Particle previous, Particle present, double deltaT, double currTime) {
        if (present == null) {
            throw new NullPointerException();
        }
        if (previous == null) {
            // Resolver con euler
            previous = euler.update(null, present, -deltaT,  currTime);
        }
        double mass = present.getMass();
        double newPosX = 2 * present.getPosX() - previous.getPosX() + Math.pow(deltaT, 2) * present.getAccX();
        double newPosY = 2 * present.getPosY() - previous.getPosY() + Math.pow(deltaT, 2) * present.getAccY();

        double newVelX = (newPosX - previous.getPosX()) / (2 * deltaT);
        double newVelY = (newPosY - previous.getPosY()) / (2 * deltaT);

        present.setVelX(newVelX);
        present.setVelY(newVelY);
        Particle future = present.clone();

        future.setPosX(newPosX);
        future.setPosY(newPosY);
        future.setAccX((-K * future.getPosX() - gamma * future.getVelX()) / mass);
        future.setAccY((-K * future.getPosY() - gamma * future.getVelY()) / mass);

        return future;
    }

    @Override
    public String getName() {
        return "verlet";
    }
}