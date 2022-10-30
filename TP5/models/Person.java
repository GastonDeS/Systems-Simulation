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
    protected Point vel;
    protected Point pos;
    protected double radius;
    protected PersonState state;
    private double timeLeft;
    protected Point desiredPos;
    protected double desiredSpeed;
    protected double deltaAngle; // Limit angle of vision
    protected double limitVision;
    protected double Ap;
    protected double Bp;

    private final double tau;

    public Person(String id, double positionX, double positionY, Config config) {
        this.id = id;
        this.vel = new Point(0 ,0);
        this.pos = new Point(positionX, positionY);
        this.radius = Rmin;
        this.timeLeft = CONVERSION_TIME;
        this.state = PersonState.WALKING;
        this.tau = config.getTau();
    }

    protected boolean isColliding(Person person) {
        return pos.dist(person.pos) <= radius + person.radius;
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

    protected Point getRandomPos() {
        double angle = Math.random() * 2 * Math.PI;
        return new Point(Math.cos(angle) * desiredSpeed, Math.sin(angle) * desiredSpeed);
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

    protected void updateVelocityForAvoidance(Point nc) {
        Point eit = desiredPos.sub(pos).normalize();
        Point eia = nc.add(eit).normalize();
        double magnitude = desiredSpeed * Math.pow((radius - Rmin)/(Rmax - Rmin), beta);
        vel.setLocation(eia.prod(magnitude));
    }

    protected <T extends Person> T getNearestEntity(List<T> entities) {
        T nearest = entities.get(0);
        double minDist = pos.dist(nearest.pos);
        for (T entity : entities) {
            double dist = pos.dist(entity.pos);
            if (dist < minDist) {
                minDist = dist;
                nearest = entity;
            }
        }
        return nearest;
    }

    protected Point calculateEij(Point obstacle) {
        return pos.sub(obstacle).normalize();
    }

    protected Point calculateHij(Point Eij, double Ap, double Bp) {
        double mul = Ap * Math.exp(-pos.dist(Eij)/Bp);
        return new Point(Eij.x * mul, Eij.y * mul);
    }

    protected Optional<Point> handleAvoidance(List<Human> humans, List<Zombie> zombies) {
        return Optional.empty();
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
    protected abstract <T extends Person> Optional<Point> getGoalPosition(List<T> persons);

    protected abstract Optional<Point> handleCollisions(List<Human> humans);

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
