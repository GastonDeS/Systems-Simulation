package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private final int N;
    private final double deltaT;
    private List<Person> persons;
    private List<Human> humans;
    private List<Zombie> zombies;
    private final static double wallRadius = 11; // fixed value

    private final static double minRadius = 0.1;
    private final static double maxRadius = 0.3;

    public Room(final Config config) {
        this.N = config.getN();
        this.deltaT = config.getDeltaT();
        this.humans = new ArrayList<>();
        this.zombies = new ArrayList<>();
        this.persons = new ArrayList<>();
    }

    public void update() {
        for (Human h : humans) {
            handleState(h);
        }
        for (Zombie z : zombies) {
            handleState(z);
        }
    }

    private void handleState(Person p) {
        switch (p.getState()) {
            case WALKING:
                p.update(deltaT, zombies, humans);
                break;
            case CONVERTING:
                p.reduceTimeLeft(deltaT);
                if (p instanceof Human) {
                    updateLists((Human) p);
                }
                break;
        }
    }

    private void updateLists(Human h) {
        if (h.getTimeLeft() <= 0) {
            Zombie z = new Zombie(h.id, h.pos.x, h.pos.y);
            humans.remove(h);
            persons.remove(h);
            zombies.add(z);
            persons.add(z);
        }
    }

    public void fillRoom() {
        // Add zombie
        zombies.add(new Zombie(String.valueOf(0),0,0));

        // Add humans
        for (int i = 1; i < N; i++) {
            humans.add(createHuman(i));
        }

        persons.addAll(zombies);
        persons.addAll(humans);
    }

    private Human createHuman(int i) {
        double angle = Math.random() * 2 * Math.PI;
        // 11 - 1 (radio de lejania inicial al zombie) - 2 * radius (radio de la persona y el zombie estan considerados)
        double distance = (Math.random() * (wallRadius - 1 - 2 * minRadius)) + 1 + 2 * minRadius; // TODO min radius or max radius?
        Human human = new Human(
                String.valueOf(i),
                Math.cos(angle) * distance,
                Math.sin(angle) * distance
        );

        if (hasContactWithHumans(human)) return createHuman(i);
        return human;
    }

    private double getRandomRadius() {
        return Math.random() * (maxRadius - minRadius) + minRadius;
    }

    private boolean hasContactWithHumans(Human human) {
        return humans.stream().anyMatch(h -> h.isColliding(human));
    }

    private boolean hasContactWithZombie(Human h) {
        for (Zombie z : zombies) {
            if (z.isColliding(h)) {
                return true;
            }
        }
        return false;
    }

    public void savePersons(int iteration) {
        try {
            File positions = new File(String.format("TP5/position/positions%d.xyz", iteration));
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
}
