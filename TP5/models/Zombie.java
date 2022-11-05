package models;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Zombie extends Person {
    private final static double INACTIVE_SPEED = 0.3;
    private final double Vdz;
    private List<Human> nearestHumans;

    public Zombie(String id, double positionX, double positionY, double velX, double velY,Config config) {
        super(id, positionX, positionY, velX,velY, config);
        this.deltaAngle = Math.PI / 8;
        this.limitVision = 2;
        this.Vdz = config.getVdz();
        this.Ap = config.getApZombie();
        this.Bp = config.getBpZombie();
    }

    /*
        ABSTRACT METHODS
     */

    @Override
    protected void getDesiredPos(List<Zombie> zombies, List<Human> humans) {
        // Get desired target if human is near
        Optional<Point> maybeGoal = getGoalPosition(humans);
        handleConversion(maybeGoal);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected  <T extends Person> Optional<Point> getGoalPosition(List<T> humans) {
        double angle = getDirectionForSpeed();
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
    protected Point getVeFromEij(Point eij) {
        return new Point(
                -Vdz * eij.x,
                -Vdz * eij.y
        );
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
            if (desiredPos == null || pos.equals(desiredPos))
                this.desiredPos = getRandomPos();
        }
    }

    @Override
    public String toString() {
        return super.toString() +" "+ "Zombie";
    }
}