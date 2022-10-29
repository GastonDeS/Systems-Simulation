package models;

import java.util.List;
import java.util.Optional;

public class Zombie extends Person {
    private final double zombieVision = 4.0; // fixed value

    public Zombie(String id, double positionX, double positionY) {
        super(id, positionX, positionY);
    }

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        Optional<Human> maybeHuman = getNearestHuman(humans);
        if (maybeHuman.isPresent()) {
            Human human = maybeHuman.get();
            if (isColliding(human)) {
                startConversion();
                human.startConversion();
            }
        }
    }

    /*
        GETTERS
     */

    public double getZombieVision() {
        return zombieVision;
    }
}