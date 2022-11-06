package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private final Config config;
    private final List<Human> humans;
    private final List<Zombie> zombies;
    private final List<Human> converting;
    private final static double wallRadius = 11; // fixed value

    private final static double minRadius = 0.1;
    private final static double maxRadius = 0.3;

    public Room(final Config config) {
        this.config = config;
        this.humans = new ArrayList<>();
        this.zombies = new ArrayList<>();
        this.converting = new ArrayList<>();
    }

    public void update(double deltaT) {
        for (int i = 0; i < humans.size(); i++) {
            Human h = humans.get(i);
            if (handleState(h, deltaT)) {
                i--;
            }
        }
        for (Zombie z : zombies) {
            handleState(z, deltaT);
        }
        humans.forEach(p -> p.updateRadiusAndPosition(deltaT));
        zombies.forEach(p -> p.updateRadiusAndPosition(deltaT));
    }

    /**
     *
     * @param p      current person
     * @param deltaT simulation time for update
     * @return returns true if the person has changed to a zombie
     */
    private boolean handleState(Person p, double deltaT) {
        switch (p.getState()) {
            case WALKING:
                p.update(deltaT, zombies, humans, converting);
                break;
            case CONVERTING:
                p.reduceTimeLeft(deltaT);
                if (p instanceof Human) {
                    return updateLists((Human) p);
                }
                break;
        }
        return false;
    }

    private boolean updateLists(Human h) {
        if (h.getTimeLeft() <= 0) {
            Zombie z = new Zombie(h.getId(), h.pos.x, h.pos.y, h.vel.x, h.vel.y,config);
            humans.remove(h);
            converting.remove(h);
            zombies.add(z);
            return true;
        }
        return false;
    }

    public void fillRoom() {
        // Add zombie
        double angle = Math.random() * 2 * Math.PI;
        double speed = 0.3;
        zombies.add(new Zombie(String.valueOf(0),0,0, Math.cos(angle) * speed, Math.sin(angle) * speed, config));

        // Add humans
        for (int i = 1; i < config.getNh(); i++) {
            humans.add(createHuman(i));
        }

    }

    private Human createHuman(int i) {
        double angle = Math.random() * 2 * Math.PI;
        // 11 - 1 (radio de lejania inicial al zombie) - 2 * radius (radio de la persona y el zombie estan considerados)
        double distance = (Math.random() * (wallRadius - 2)) + 1;
        Human human = new Human(
                String.valueOf(i),
                Math.cos(angle) * distance,
                Math.sin(angle) * distance,
                Math.sin(angle) * distance, // puesto al revez para hacerlo distinto pq si
                Math.cos(angle) * distance,
                config
        );

        if (hasContactWithHumans(human)) return createHuman(i);
        return human;
    }

    private boolean hasContactWithHumans(Human human) {
        return humans.stream().anyMatch(h -> h.isColliding(human));
    }

    public double getHumanZombieRatio() {
        return (double) (zombies.size() + converting.size()) / (zombies.size() + humans.size());
    }

    public int getZombieCount() {
        return zombies.size();
    }

    public void savePersons(int iteration) {
        try {
            File positions = new File(String.format("TP5/../../position/positions%d.xyz", iteration));
            FileWriter positionsFile = new FileWriter(positions);

            positionsFile.write((1 + zombies.size() + humans.size()) + "\n" +
                    "Lattice=\"1 0.0 0.0 0.0 1 0.0 0.0 0.0 1\"" +
                    "\n");
            positionsFile.write("-1 0 0 11 Background\n");
            for (Zombie z : zombies) {
                positionsFile.write(z + "\n");
            }
            for (Human h : humans) {
                positionsFile.write(h + "\n");
            }

            positionsFile.close();
        } catch (IOException e) {
            System.out.println("Add folder TP5/position");
        }
    }

    public static double getWallRadius() {
        return wallRadius;
    }

    public List<Human> getHumans() {
        return humans;
    }
}
