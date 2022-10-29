package models;

import java.util.List;
import java.util.Optional;

public class Human extends Person {

    public Human(String id, double positionX, double positionY) {
        super(id, positionX, positionY);
    }

    @Override
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        Optional<Zombie> maybeZombie = getNearestZombie(zombies);
        if (maybeZombie.isPresent()) {
            Zombie zombie = maybeZombie.get();
            if (dist(zombie) < zombie.getZombieVision()) {
                // update desired pos because human being attacked
            }
        } else {
            // TODO: check if we should avoid collision with another human
            desiredPos = pos;
            //radius = Rmin;
        }
    }

    public void setInfected() {
        this.state = PersonState.INFECTED;
    }
}
