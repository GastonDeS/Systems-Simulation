package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Zombie extends Person {
    private final static double INACTIVE_SPEED = 0.3;
    private final double Vdz;
    private List<Human> nearestHumans;

    public Zombie(String id, double positionX, double positionY, Config config) {
        super(id, positionX, positionY, config);
        this.deltaAngle = Math.PI / 8;
        this.limitVision = 4;
        this.Vdz = config.getVdz();
    }

    /*
        ABSTRACT METHODS
     */

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        // Update position
        pos.setLocation(vel.x * deltaT, vel.y * deltaT);

        // Get desired target if human is near
        Optional<Point2D.Double> maybeGoal = getGoalPosition(humans);
        handleConversion(maybeGoal);

        // Check if there are collisions and get escape velocity
        Optional<Point2D.Double> Ve = handleCollisions(humans);

        if (Ve.isPresent()) {
            vel.setLocation(Ve.get());
            radius = Rmin;
        } else  {
            // TODO: handle avoidance
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected  <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> humans) {
        double angle = Math.atan(vel.y/ vel.x);
        nearestHumans = (List<Human>) humans.stream()
                .filter(h -> this.isOnVision(h, angle, deltaAngle, limitVision))
                .collect(Collectors.toList());

        return nearestHumans.stream()
                .map(h -> h.pos).min((p1, p2) -> {
                    double distance1 = Math.sqrt(Math.pow(pos.x - p1.x, 2) + Math.pow(pos.y - p1.y, 2));
                    double distance2 = Math.sqrt(Math.pow(pos.x - p2.x, 2) + Math.pow(pos.y - p2.y, 2));
                    return Double.compare(distance1, distance2);
                });
    }

    @Override
    protected Optional<Point2D.Double> handleCollisions(List<Human> humans) {
        Optional<Point2D.Double> Ve = Optional.empty();

        if (isTouchingCircularWall(Room.getWallRadius())) {
            double newVelAngle = Math.atan(vel.y / vel.x) + Math.PI/3;
            Ve = Optional.of(new Point2D.Double(
                    Math.cos(newVelAngle) * desiredSpeed,
                    Math.sin(newVelAngle) * desiredSpeed
            ));
        }

        // TODO: should we check collision with other zombies??
        return Ve;
    }

    @Override
    protected Point2D.Double handleAvoidance(List<Human> humans) {
        return null;
    }

    /*
        UTILS
     */

    private Human getNearestHuman() {
        Human nearestHuman = nearestHumans.get(0);
        double minDist = dist(nearestHuman);

        for (Human h : nearestHumans) {
            if (dist(h) < minDist) {
                minDist = dist(h);
                nearestHuman = h;
            }
        }

        return nearestHuman;
    }

    private void handleConversion(Optional<Point2D.Double> maybeGoal) {
        if (maybeGoal.isPresent()) {
            Human nearestHuman = getNearestHuman();
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
    }
}