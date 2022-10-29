package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public class Zombie extends Person {

    public Zombie(String id, double positionX, double positionY, double radius) {
        super(id, positionX, positionY, radius, true);
        deltaAngle = Math.PI / 8; // angulo de vision
        limitVision = 4; // largo de vision
    }

    @Override
    protected void update(double deltaT) {

    }

    public <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> humans) {
        double angle = Math.atan(vel.y/ vel.x);

        return humans.stream()
                .filter(h -> this.isOnVision(h, angle, deltaAngle, limitVision))
                .map(h -> h.pos).min((p1, p2) -> {
                    double distance1 = Math.sqrt(Math.pow(pos.x - p1.x, 2) + Math.pow(pos.y - p1.y, 2));
                    double distance2 = Math.sqrt(Math.pow(pos.x - p2.x, 2) + Math.pow(pos.y - p2.y, 2));
                    return Double.compare(distance1, distance2);
                });
    }
}