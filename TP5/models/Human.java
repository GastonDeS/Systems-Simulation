package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Human extends Person {

    public Human(String id, double positionX, double positionY) {
        super(id, positionX, positionY);
        deltaAngle = Math.PI / 4; // angulo de vision
        limitVision = 6; // largo de vision
    }

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        Optional<Zombie> maybeZombie = getNearestZombie(zombies);
        if (maybeZombie.isPresent()) {
            Zombie zombie = maybeZombie.get();
            if (dist(zombie) < zombie.limitVision) {
                // update desired pos because human being attacked
            }
        } else {
            // TODO: check if we should avoid collision with another human
            desiredPos = pos;
            //radius = Rmin;
        }
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
