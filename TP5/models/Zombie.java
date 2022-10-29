package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Zombie extends Person {
    private final static double INACTIVE_SPEED = 0.3;
    private final double Vdz;

    public Zombie(String id, double positionX, double positionY, Config config) {
        super(id, positionX, positionY);
        this.deltaAngle = Math.PI / 8; // angulo de vision
        this.limitVision = 4; // largo de vision
        this.Vdz = config.getVdz();
    }

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        double angle = Math.atan(vel.y/ vel.x);
        List<Human> nearHumans = humans.stream()
                .filter(h -> this.isOnVision(h, angle, deltaAngle, limitVision)).collect(Collectors.toList());
        if (nearHumans.size() > 0) {
            Human nearestHuman = getNearestHuman(humans);
            if (isColliding(nearestHuman)) {
                startConversion();
                nearestHuman.startConversion();
            }
            this.desiredPos = nearestHuman.pos;
            this.desiredSpeed = Vdz;
        } else {
            this.desiredSpeed = INACTIVE_SPEED;
            this.desiredPos = getRandomPos();
        }
        //TODO: check if zombie hits another zombie?
    }

    private Human getNearestHuman(List<Human> humans) {
        Human nearestHuman = humans.get(0);
        double minDist = dist(nearestHuman);

        for (Human h : humans) {
            if (dist(h) < minDist) {
                minDist = dist(h);
                nearestHuman = h;
            }
        }

        return nearestHuman;
    }

    public <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> humans) {
        return humans.stream()
                .map(h -> h.pos).min((p1, p2) -> {
                    double distance1 = Math.sqrt(Math.pow(pos.x - p1.x, 2) + Math.pow(pos.y - p1.y, 2));
                    double distance2 = Math.sqrt(Math.pow(pos.x - p2.x, 2) + Math.pow(pos.y - p2.y, 2));
                    return Double.compare(distance1, distance2);
                });
    }
}