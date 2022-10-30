package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Person {
    // TODO: check values
    protected final static double CONVERSION_TIME = 7.0;
    protected final static double Rmin = 0.1;
    protected final static double Rmax = 0.3;
    protected static final double beta = 0.5;

    private final String id;
    protected Point2D.Double vel;
    protected Point2D.Double pos;
    protected double radius;
    protected PersonState state;
    private double timeLeft;
    protected Point2D.Double desiredPos;
    protected double desiredSpeed;
    protected double deltaAngle; // Limit angle of vision
    protected double limitVision;
    protected double Ap;
    protected double Bp;

    private final double tau;

    public Person(String id, double positionX, double positionY, Config config) {
        this.id = id;
        this.vel = new Point2D.Double(0 ,0);
        this.pos = new Point2D.Double(positionX, positionY);
        this.radius = Rmin;
        this.timeLeft = CONVERSION_TIME;
        this.state = PersonState.WALKING;
        this.tau = config.getTau();
    }

    protected double dist(Person p) {
        return Math.sqrt(Math.pow(pos.x - p.pos.x, 2) + Math.pow(pos.y - p.pos.y, 2));
    }

    protected double norm() {
        return Math.sqrt(Math.pow(pos.x, 2) + Math.pow(pos.y, 2));
    }

    protected boolean isColliding(Person person) {
        double distance = Math.sqrt(Math.pow(pos.x - person.pos.x, 2) + Math.pow(pos.y - person.pos.y, 2));
        return distance <= radius + person.radius;
    }

    protected boolean isTouchingCircularWall(double wallRadius) {
        return Math.sqrt(Math.pow(pos.x, 2) + Math.pow(pos.y, 2)) >= wallRadius - radius;
    }

    protected void startConversion() {
        this.state = PersonState.CONVERTING;
    }

    protected void reduceTimeLeft(double deltaT) {
        this.timeLeft -= deltaT;
        if (this.timeLeft <= 0) {
            this.state = PersonState.WALKING;
            this.timeLeft = CONVERSION_TIME;
        }
    }

    protected Point2D.Double getRandomPos() {
        double angle = Math.random() * 2 * Math.PI;
        return new Point2D.Double(Math.cos(angle) * desiredSpeed, Math.sin(angle) * desiredSpeed);
    }

    protected boolean isOnVision(Person person, double angle) {
        double angleBetweenEntities = Math.atan((person.pos.y - pos.y) / (person.pos.x - pos.x));

        return (angleBetweenEntities <= angle + deltaAngle
                && angleBetweenEntities >= angle - deltaAngle)
                && (Math.sqrt(Math.pow(person.pos.x - pos.x, 2) + Math.pow(person.pos.y - pos.y, 2)) <= limitVision);
    }

    protected void updateRadius(double deltaT) {
        if (radius < Rmax) {
            radius += Rmax / (tau / deltaT);
        } else {
            radius = Rmax;
        }
    }

    protected <T extends Person> Optional<T> getNearestEntity(List<T> entities) {
        double angle = Math.atan(vel.y/ vel.x);
        List<T> entitiesOnSight = entities.stream()
                .filter(h -> h != this && this.isOnVision(h, angle))
                .collect(Collectors.toList());

        Optional<T> nearest = Optional.empty();
        double minDist = Double.MAX_VALUE;
        for (T entity : entitiesOnSight) {
            double dist = dist(entity);
            if (dist < minDist) {
                minDist = dist;
                nearest = Optional.of(entity);
            }
        }
        return nearest;
    }

    protected <T extends Person> Point2D.Double calculateEij(T entity) {
        entity.pos.setLocation(pos.x - entity.pos.x, pos.y - entity.pos.y);
        double norm = entity.norm();
        return new Point2D.Double(entity.pos.x/norm, entity.pos.y/norm);
    }

    protected Point2D.Double calculateHij(Point2D.Double Eij, double distance, double Ap, double Bp) {
        double mul = Ap * Math.exp(-distance/Bp);
        return new Point2D.Double(Eij.x * mul, Eij.y * mul);
    }

    /**
     * Update position and velocity of humans and zombies
     * @param deltaT
     * @param zombies
     * @param humans
     */
    protected abstract void update(double deltaT, List<Zombie> zombies, List<Human> humans);

    /**
     * @param persons if the person is a zombie, the list has to be humans,
     *               if the person is a human, the list has to be zombies
     * @return the goal position of the person
     */
    protected abstract <T extends Person> Optional<Point2D.Double> getGoalPosition(List<T> persons);

    protected abstract Optional<Point2D.Double> handleCollisions(List<Human> humans);

    protected abstract Optional<Point2D.Double> handleAvoidance(List<Human> humans, List<Zombie> zombies);

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
