package models;

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
        this.Ap = config.getApZombie();
        this.Bp = config.getBpZombie();
    }

    /*
        ABSTRACT METHODS
     */

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        // Get desired target if human is near
        Optional<Point> maybeGoal = getGoalPosition(humans);
        handleConversion(maybeGoal);

        // Check if there are collisions and get escape velocity
        Optional<Point> Ve = handleCollisions(humans);

        if (Ve.isPresent()) {
            vel.setLocation(Ve.get());
            radius = Rmin;
        } else  {
            updateRadius(deltaT);
            updateVelocityForAvoidance(new Point(0,0));
        }

        // Update position
        pos.setLocation(vel.prod(deltaT));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected  <T extends Person> Optional<Point> getGoalPosition(List<T> humans) {
        double angle = Math.atan(vel.y/ vel.x);
        nearestHumans = (List<Human>) humans.stream()
                .filter(h -> this.isOnVision(h.pos, angle))
                .collect(Collectors.toList());

        return nearestHumans.stream()
                .map(h -> h.pos).min((p1, p2) -> {
                    double distance1 = Math.sqrt(Math.pow(pos.x - p1.x, 2) + Math.pow(pos.y - p1.y, 2));
                    double distance2 = Math.sqrt(Math.pow(pos.x - p2.x, 2) + Math.pow(pos.y - p2.y, 2));
                    return Double.compare(distance1, distance2);
                });
    }

    @Override
    protected Optional<Point> handleCollisions(List<Human> humans) {
        Optional<Point> Ve = Optional.empty();

        if (isTouchingCircularWall(Room.getWallRadius())) {
            double newVelAngle = Math.atan(vel.y / vel.x) + Math.PI/3;
            Ve = Optional.of(new Point(
                    Math.cos(newVelAngle) * desiredSpeed,
                    Math.sin(newVelAngle) * desiredSpeed
            ));
        }
        return Ve;
    }

    /*
        UTILS
     */

    private void handleConversion(Optional<Point> maybeGoal) {
        if (maybeGoal.isPresent()) {
            Human nearestHuman = getNearestEntity(nearestHumans);
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