package utils.algorithms;

import utils.Particle;
import utils.UpdateMethods;

import java.awt.geom.Point2D;

public class BeemanAlgorithm implements Algorithm {
    private final double K;
    private final double gamma;
    private final Algorithm euler;

    private final UpdateMethods name = UpdateMethods.BEEMAN;

    @Override
    public String getName() {
        return name.name;
    }

    public BeemanAlgorithm(double K, double gamma) {
        this.K = K;
        this.gamma = gamma;
        this.euler = new EulerAlgorithm(K, gamma);
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT) {
        if(current == null) throw new NullPointerException("No current particle to update with");
        if(previous == null) previous = euler.update(null, current, -deltaT);
        Particle next = current.clone();

        Point2D.Double nextPos = getNextPos(previous, current, deltaT);
        Point2D.Double predictedVel = getPredictedVel(previous, current, deltaT);
        Point2D.Double predictedAcc = getAcc(nextPos, predictedVel, current.getMass());

        Point2D.Double correctedVel = getCorrectedVel(previous, current, deltaT, predictedAcc);
        Point2D.Double correctedAcc = getAcc(nextPos, correctedVel, current.getMass());

        next.setPosX(nextPos.x);
        next.setPosY(nextPos.y);
        next.setVelX(correctedVel.x);
        next.setVelY(correctedVel.y);
        next.setAccX(correctedAcc.x);
        next.setAccY(correctedAcc.y);

        return next;
    }

    private Point2D.Double getNextPos(Particle previous, Particle current, double deltaT) {
        double posX = current.getPosX() + current.getVelX() * deltaT + (2.0/3) * current.getAccX() * Math.pow(deltaT, 2)
                - (1.0/6) * previous.getAccX() * Math.pow(deltaT, 2);
        double posY = current.getPosY() + current.getVelY() * deltaT + (2.0/3) * current.getAccY() * Math.pow(deltaT, 2)
                - (1.0/6) * previous.getAccY() * Math.pow(deltaT, 2);
        return new Point2D.Double(posX, posY);
    }

    private Point2D.Double getPredictedVel(Particle previous, Particle current, double deltaT) {
        double velX = current.getVelX() + 1.5 * current.getAccX() * deltaT - 0.5 * previous.getAccX() * deltaT;
        double velY = current.getVelY() + 1.5 * current.getAccY() * deltaT - 0.5 * previous.getAccY() * deltaT;
        return new Point2D.Double(velX, velY);
    }

    private Point2D.Double getCorrectedVel(Particle previous, Particle current, double deltaT, Point2D.Double predictedAcc) {
        double velX = current.getVelX() + (1.0/3) * predictedAcc.x * deltaT + (5.0/6) * current.getAccX() * deltaT - (1.0/6) * previous.getAccX() * deltaT;
        double velY = current.getVelY() + (1.0/3) * predictedAcc.y * deltaT + (5.0/6) * current.getAccY() * deltaT - (1.0/6) * previous.getAccY() * deltaT;
        return new Point2D.Double(velX, velY);
    }

    private Point2D.Double getAcc(Point2D.Double pos, Point2D.Double vel, double mass) {
        double accX = (-K * pos.x - gamma * vel.x)/mass;
        double accY = (-K * pos.y - gamma * vel.y)/mass;
        return new Point2D.Double(accX, accY);
    }

}
