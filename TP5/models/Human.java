package models;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Human extends Person {
    private static final double Vdh = 4; // TODO: check if Vdh is escape velocity magnitude

    public Human(String id, double positionX, double positionY, double velX, double velY, Config config) {
        super(id, positionX, positionY, velX, velY,config);
        this.deltaAngle = Math.PI / 4;
        this.limitVision = 2;
        this.desiredSpeed = 4;
        this.Ap = config.getApHuman();
        this.Bp = config.getBpHuman();
    }

    /*
        ABSTRACT METHODS
     */

    @Override
    protected void getDesiredPos(List<Zombie> zombies, List<Human> humans, List<Human> converting) {
        // Get desired target if human is threatened
        Optional<Point> maybeGoal = getGoalPosition(zombies);
        desiredPos = maybeGoal.orElseGet(() -> pos.add(vel));
    }

    @Override
    protected Point getVeFromEij(Point eij) {
        return new Point(
                -Vdh * eij.x,
                -Vdh * eij.y
        );
    }

    @Override
    protected Point handleAvoidance(List<Human> humans, List<Zombie> zombies) {
        double angle = getDirectionForSpeed();

        Optional<Point> ncWall = handleAvoidNearestWall(); // TODO esquivar para un lado o el otro
        Optional<Point> ncHuman = handleAvoidNearestHuman(angle, humans);
        Optional<Point> ncZombie = handleAvoidNearestZombie(angle, zombies);

        Point nc = new Point(0,0);
        if (ncWall.isPresent()) {
            nc = nc.sub(ncWall.get());
        }
        if (ncHuman.isPresent()) {
            nc = nc.sub(ncHuman.get());
        }
        if (ncZombie.isPresent()) {
            nc = nc.add(ncZombie.get());
        }

        return nc;
    }

    @Override
    protected <T extends Person> Optional<Point> getGoalPosition(List<T> zombies) {
        double angle = getDirectionForSpeed();

        List<Double> zombieAngles = zombies.stream()
                .filter(z -> this.isOnVision(z.pos, angle))
                .map(z -> getDirection((z.pos.x - pos.x), (z.pos.y - pos.y)))
                .collect(Collectors.toList());

        if (zombieAngles.isEmpty()) return Optional.empty();

        double desiredAngle = Math.PI + zombieAngles.stream().reduce(Double::sum).get() / zombieAngles.size();

        return Optional.of(
                new Point(
                        Math.cos(desiredAngle) *2 + pos.x,
                        Math.sin(desiredAngle) *2+ pos.y));
    }

    /*
        UTILS
     */

    private Optional<Point> handleAvoidNearestWall() {
        Optional<Point> nc = Optional.empty();

        Optional<Point> maybeWall = getNearestWallOnSight();
        if (maybeWall.isPresent()) {
            nc = Optional.of(calculateHij(calculateEij(maybeWall.get()), ApWall, BpWall));
        }

        return nc;
    }

    private Optional<Point> handleAvoidNearestHuman(double angle, List<Human> humans) {
        Optional<Point> nc = Optional.empty();

        List<Human> humansOnSight = humans.stream()
                .filter(h -> h != this && this.isOnVision(h.pos, angle))
                .collect(Collectors.toList());
        if (humansOnSight.size() > 0) {
            Human human = getNearestEntity(humansOnSight);
            nc = Optional.of(calculateHij(calculateEij(human.pos), human.Ap, human.Bp));
        }

        return nc;
    }

    private Optional<Point> handleAvoidNearestZombie(double angle, List<Zombie> zombies) {
        Optional<Point> nc = Optional.empty();

        List<Zombie> zombiesOnSight = zombies.stream()
                .filter(h -> this.isOnVision(h.pos, angle))
                .collect(Collectors.toList());
        if (zombiesOnSight.size() > 0) {
            Zombie zombie = getNearestEntity(zombiesOnSight);
            nc = Optional.of(calculateHij(calculateEij(zombie.pos), zombie.Ap, zombie.Bp));
        }

        return nc;
    }

    @Override
    public String toString() {
        return super.toString() +" "+ "Human";
    }
}
