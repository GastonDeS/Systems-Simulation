package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public abstract class Person {
    protected final static double TO_ZOMBIE_TIME = 7.0;
    protected final static double Rmin = 0.2;
    protected final static double Rmax = 0.3;
    protected final String id;
    protected Point2D.Double vel;
    protected Point2D.Double pos;
    protected Point2D.Double desiredPos;
    protected double radius;
    protected PersonState state;
    private double timeLeft;

    public Person(String id, double positionX, double positionY) {
        this.id = id;
        this.vel = new Point2D.Double(0 ,0);
        this.pos = new Point2D.Double(positionX, positionY);
        this.radius = Rmin;
        this.timeLeft = TO_ZOMBIE_TIME;
        this.state = PersonState.WALKING;
    }

    protected double dist(Person p) {
        return Math.sqrt(Math.pow(pos.x - p.pos.x, 2) + Math.pow(pos.y - p.pos.y, 2));
    }

    protected boolean isColliding(Person person) {
        double distance = Math.sqrt(Math.pow(pos.x - person.pos.x, 2) + Math.pow(pos.y - person.pos.y, 2));
        return distance <= radius + person.radius;
    }

    protected boolean isTouchingCircularWall(double wallRadius) {
        return Math.sqrt(Math.pow(pos.x, 2) + Math.pow(pos.y, 2)) >= wallRadius - radius;
    }

    protected Optional<Zombie> getNearestZombie(List<Zombie> zombies) {
        Optional<Zombie> nearestZombie = Optional.empty();
        double minDist = Double.MAX_VALUE;

        for (Zombie z : zombies) {
            if (z == this) continue;
            double dist = dist(z);
            if (dist < minDist) {
                minDist = dist;
                nearestZombie = Optional.of(z);
            }
        }
        return nearestZombie;
    }

    protected Optional<Human> getNearestHuman(List<Human> humans) {
        Optional<Human> nearestHuman = Optional.empty();
        double minDist = Double.MAX_VALUE;

        for (Human h : humans) {
            if (h == this) continue;
            double dist = dist(h);
            if (dist < minDist) {
                minDist = dist;
                nearestHuman = Optional.of(h);
            }
        }
        return nearestHuman;
    }

    protected void startConversion() {
        this.state = PersonState.CONVERTING;
    }

    protected void reduceTimeLeft(double deltaT) {
        this.timeLeft -= deltaT;
        if (this.timeLeft <= 0) {
            this.state = PersonState.WALKING;
            this.timeLeft = TO_ZOMBIE_TIME;
        }
    }

    protected abstract void update(double deltaT, List<Zombie> zombies, List<Human> humans);

    @Override
    public String toString() {
        return id + " " + pos.x + " " + pos.y + " " + radius;
    }

    /*
        GETTERS
     */

    public double getTimeLeft() {
        return timeLeft;
    }

    public PersonState getState() {
        return state;
    }
}
