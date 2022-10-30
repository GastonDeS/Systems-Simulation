package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Human extends Person {
    private static final double Vdh = 4; // TODO: check if Vdh is escape velocity magnitude

    public Human(String id, double positionX, double positionY, Config config) {
        super(id, positionX, positionY, config);
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
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        // Update position
        pos.setLocation(vel.x * deltaT, vel.y * deltaT);

        // Get desired target if human is threatened
        Optional<Point2D.Double> maybeGoal = getGoalPosition(zombies);
        desiredPos.setLocation(maybeGoal.orElseGet(() -> pos));

        // Check if there are collisions and get escape velocity
        Optional<Point2D.Double> Ve = handleCollisions(humans);

        if (Ve.isPresent()) {
            vel.setLocation(Ve.get());
            radius = Rmin;
        } else {
            updateRadius(deltaT);
            // TODO change for PPT7 30/31
            double intensity = (desiredSpeed * Math.pow((radius-Rmin)/(Rmax-Rmin), beta));
            double newAngle = Math.atan(vel.y / vel.x);
            if (maybeGoal.isPresent()) {
                newAngle = Math.tan(maybeGoal.get().y - pos.y / maybeGoal.get().x - pos.x);
            }
            vel.x = intensity * Math.cos(newAngle);
            vel.y = intensity * Math.sin(newAngle);
        }
    }

    @Override
    protected Optional<Point2D.Double> handleCollisions(List<Human> humans) {
        List<Human> humansColliding = humans.stream()
                .filter(this::isColliding)
                .collect(Collectors.toList());

        Optional<Point2D.Double> Ve = Optional.empty();

        if (isTouchingCircularWall(Room.getWallRadius())) {
            double newVelAngle = Math.atan(vel.y / vel.x) + Math.PI/3;
            Ve = Optional.of(new Point2D.Double(
                    Math.cos(newVelAngle) * desiredSpeed,
                    Math.sin(newVelAngle) * desiredSpeed
            ));
        } else if (!humansColliding.isEmpty()) {
            Point2D.Double eij = getEij(humansColliding);
            Ve = Optional.of(new Point2D.Double(
                    Vdh * eij.x,
                    Vdh * eij.y
            ));
        }

        return Ve;
    }

    @Override
    protected Optional<Point2D.Double> handleAvoidance(List<Human> humans, List<Zombie> zombies) {
        Optional<Point2D.Double> nc = Optional.empty();

        Optional<Human> nearestHuman = getNearestEntity(humans);
        if (nearestHuman.isPresent()) {
            Human human = nearestHuman.get();
            nc = Optional.of(calculateHij(calculateEij(human), dist(human), human.Ap, human.Bp));
        }

        Optional<Zombie> nearestZombie = getNearestEntity(zombies);
        if (nearestZombie.isPresent()) {
            Zombie zombie = nearestZombie.get();
            if (nc.isPresent()) {
                Point2D.Double nearPos = calculateHij(calculateEij(zombie), dist(zombie), zombie.Ap, zombie.Bp);
                nc = Optional.of(new Point2D.Double(nc.get().x + nearPos.x, nc.get().y + nearPos.y));
            } else {
                nc = Optional.of(calculateHij(calculateEij(zombie), dist(zombie), zombie.Ap, zombie.Bp));
            }
        }

        return nc;
    }

    @Override
    protected <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> zombies) {
        double angle = Math.atan(vel.y/ vel.x);

        List<Double> zombieAngles = zombies.stream()
                .filter(z -> this.isOnVision(z, angle))
                .map(z -> Math.atan((z.pos.y - pos.y) / (z.pos.x - pos.x)))
                .collect(Collectors.toList());

        if (zombieAngles.isEmpty()) return Optional.empty();

        double desiredAngle = Math.PI + zombieAngles.stream().reduce(Double::sum).get() / zombieAngles.size();

        return Optional.of(
                new Point2D.Double(
                        Math.cos(desiredAngle) * Room.getWallRadius() + pos.x, // TODO analize if it is better to pass
                        Math.sin(desiredAngle) * Room.getWallRadius() + pos.y)); // TODO the wall radius instead of this
    }

    /*
        UTILS
     */

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
}
