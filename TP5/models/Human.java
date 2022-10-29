package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Human extends Person {

    private static double vd = 4;
    private static final double beta = 0.5; // TODO definir
    private static final double ve = 0.5; // TODO definir
    private static final double tau = 3.14; // TODO definir

    public Human(String id, double positionX, double positionY) {
        super(id, positionX, positionY);
        deltaAngle = Math.PI / 4; // angulo de vision
        limitVision = 6; // largo de vision
    }

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        // update position
        pos.x += vel.x * deltaT;
        pos.y += vel.y * deltaT;

        // update velocity
        Optional<Point2D.Double> maybeGoal = getGoalPosition(zombies);

        List<Human> humansColliding = humans.stream()
                .filter(this::isColliding)
                .collect(Collectors.toList());

        if (radius < Rmax) {
            radius += Rmax / (tau / deltaT);
        }

        if (!humansColliding.isEmpty()) {
            Point2D.Double eij = getEij(humansColliding);
            vel.x = ve * eij.x;
            vel.y = ve * eij.y;
            radius = Rmin;
        } else if (maybeGoal.isPresent()) {
            vel.x = vd * Math.pow((radius-Rmin)/(Rmax-Rmin), beta);
            vel.y = vd * Math.pow((radius-Rmin)/(Rmax-Rmin), beta);
        } else if (isTouchingCircularWall(Room.getWallRadius())) {
            double newVelAngle = Math.atan(vel.y / vel.x) + Math.PI /3;
            vel.x = Math.cos(newVelAngle) * vd;
            vel.y = Math.sin(newVelAngle) * vd;
        }
    }

    private Point2D.Double getEij(List<Human> humansColliding) {
        List<Point2D.Double> IJs = getEijs(humansColliding);
        Point2D.Double IJ = new Point2D.Double(0, 0);
        for (Point2D.Double IJi : IJs) {
            IJ.x += IJi.x;
            IJ.y += IJi.y;
        } // TODO confirmar que esto es correcto
        double norm = Math.sqrt(Math.pow(IJ.x, 2) + Math.pow(IJ.y, 2));
        IJ.x /= norm;
        IJ.y /= norm;
        return IJ;
    }

    private List<Point2D.Double> getEijs(List<Human> humansColliding) {
        return humansColliding.stream()
                .map(h -> {
                    Point2D.Double eij = new Point2D.Double((h.pos.x - pos.x) , (h.pos.y - pos.y));
                    double norm = Math.sqrt(Math.pow(eij.x, 2) + Math.pow(eij.y, 2));
                    eij.x /= norm;
                    eij.y /= norm;
                    return eij;
                })
                .collect(Collectors.toList());
    }

    @Override
    public <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> zombies) {
        double angle = Math.atan(vel.y/ vel.x);

        List<Double> zombieAngles = zombies.stream()
                .filter(z -> this.isOnVision(z, angle, deltaAngle, limitVision))
                .map(z -> Math.atan((z.pos.y - pos.y) / (z.pos.x - pos.x)))
                .collect(Collectors.toList());

        if (zombieAngles.isEmpty()) return Optional.empty();

        double desiredAngle = Math.PI + zombieAngles.stream().reduce(Double::sum).get() / zombieAngles.size();

        return Optional.of(
                new Point2D.Double(
                        Math.cos(desiredAngle) * Room.getWallRadius() + pos.x, // TODO analize if it is better to pass
                        Math.sin(desiredAngle) * Room.getWallRadius() + pos.y)); // TODO the wall radius instead of this
    }
}
