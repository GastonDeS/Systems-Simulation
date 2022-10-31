package models;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Human extends Person {
    private static final double Vdh = 4; // TODO: check if Vdh is escape velocity magnitude

    public Human(String id, double positionX, double positionY, Config config) {
        super(id, positionX, positionY, 1, 1,config);
        this.deltaAngle = Math.PI / 4;
        this.limitVision = 6;
        this.desiredSpeed = 4;
        this.Ap = config.getApHuman();
        this.Bp = config.getBpHuman();
    }

    /*
        ABSTRACT METHODS
     */

    @Override
    protected void getDesiredPos(List<Zombie> zombies, List<Human> humans) {
        // Get desired target if human is threatened
        Optional<Point> maybeGoal = getGoalPosition(zombies);
        desiredPos = maybeGoal.orElseGet(() -> pos.add(vel));
    }

    @Override
    protected Optional<Point> handleCollisions(List<Human> humans) {
        List<Human> humansColliding = humans.stream()
                .filter(this::isColliding)
                .collect(Collectors.toList());

        Optional<Point> Ve = Optional.empty();

        if (isTouchingCircularWall(Room.getWallRadius())) {
            double newVelAngle = getDirectionForSpeed() + Math.PI / 3;
            Ve = Optional.of(new Point(
                    Math.cos(newVelAngle) * desiredSpeed,
                    Math.sin(newVelAngle) * desiredSpeed
            ));
        } else if (!humansColliding.isEmpty()) {
            Point eij = getEij(humansColliding);
            Ve = Optional.of(new Point(
                    -Vdh * eij.x,
                    -Vdh * eij.y
            ));
        }
        return Ve;
    }

    @Override
    protected Point handleAvoidance(List<Human> humans, List<Zombie> zombies) {
        double angle = getDirectionForSpeed();

        Optional<Point> ncWall = handleAvoidNearestWall();
        Optional<Point> ncHuman = handleAvoidNearestHuman(angle, humans);
        Optional<Point> ncZombie = handleAvoidNearestZombie(angle, zombies);

        Point nc = new Point(0,0);
        if (ncWall.isPresent()) {
            nc = nc.add(ncWall.get());
        }
        if (ncHuman.isPresent()) {
            nc = nc.add(ncHuman.get());
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
                        Math.cos(desiredAngle) * Room.getWallRadius() + pos.x, // TODO analize if it is better to pass
                        Math.sin(desiredAngle) * Room.getWallRadius() + pos.y)); // TODO the wall radius instead of this
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
        if (zombiesOnSight.size() > 1) {
            Zombie zombie = getNearestEntity(zombiesOnSight);
            nc = Optional.of(calculateHij(calculateEij(zombie.pos), zombie.Ap, zombie.Bp));
        }

        return nc;
    }

    private Point getEij(List<Human> humansColliding) {
        List<Point> IJs = getEijs(humansColliding);
        Point IJ = new Point(0, 0);
        for (Point IJi : IJs) {
            IJ.x += IJi.x;
            IJ.y += IJi.y;
        } // TODO confirmar que esto es correcto
//        double norm = Math.sqrt(Math.pow(IJ.x, 2) + Math.pow(IJ.y, 2));
        return IJ.normalize();
    }

    private List<Point> getEijs(List<Human> humansColliding) {
        return humansColliding.stream()
                .map(h -> {
                    Point eij = new Point((h.pos.x - pos.x) , (h.pos.y - pos.y));
                    double norm = Math.sqrt(Math.pow(eij.x, 2) + Math.pow(eij.y, 2));
                    eij.x /= norm;
                    eij.y /= norm;
                    return eij;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString() +" "+ "Human";
    }
}
