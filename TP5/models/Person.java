package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public abstract class Person {
    protected final static double TO_ZOMBIE_TIME = 7.0;
    protected final static double Rmin = 0.1;
    protected final static double Rmax = 0.3;
    private final String id;
    protected Point2D.Double vel;
    protected Point2D.Double pos;
    protected double radius;
    protected PersonState state;
    private double timeLeft;
    protected Point2D.Double desiredPos;
    protected double deltaAngle;
    protected double limitVision;

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

    protected boolean isOnVision(Person human, double angle, double deltaAngle, double limitVision) {
        double angleBetweenHumanAndZombie = Math.atan((human.pos.y - pos.y) / (human.pos.x - pos.x));
        return (angleBetweenHumanAndZombie <= angle + deltaAngle
                && angleBetweenHumanAndZombie >= angle - deltaAngle)
                && (Math.sqrt(Math.pow(human.pos.x - pos.x, 2) + Math.pow(human.pos.y - pos.y, 2)) <= limitVision);
    }

    /**
     * @param persons if the person is a zombie, the list has to be humans,
     *               if the person is a human, the list has to be zombies
     * @return the goal position of the person
     */
    public abstract  <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> persons);

    public Point2D.Double getVel() {
        return vel;
    }

    public void setVel(Point2D.Double vel) {
        this.vel = vel;
    }

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

    public String getId() {
        return id;
    }
}
