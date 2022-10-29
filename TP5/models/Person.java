package models;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public abstract class Person {
    private final String id;
    protected Point2D.Double vel;
    protected Point2D.Double pos;
    protected final double radius;
    private boolean isInfected;

    protected double deltaAngle;
    protected double limitVision;

    public Person(String id, double positionX, double positionY, double radius,boolean isInfected) {
        this.id = id;
        this.vel = new Point2D.Double(0 ,0);
        this.pos = new Point2D.Double(positionX, positionY);
        this.radius = radius;
        this.isInfected = isInfected;
    }

    public boolean isColliding(Person person) {
        double distance = Math.sqrt(Math.pow(pos.x - person.pos.x, 2) + Math.pow(pos.y - person.pos.y, 2));
        return distance <= radius + person.radius;
    }

    public boolean isTouchingCircularWall(double wallRadius) {
        return Math.sqrt(Math.pow(pos.x, 2) + Math.pow(pos.y, 2)) >= wallRadius - radius;
    }

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

    protected abstract void update(double deltaT);

    public Point2D.Double getVel() {
        return vel;
    }

    public void setVel(Point2D.Double vel) {
        this.vel = vel;
    }

    @Override
    public String toString() {
        return id + " " + pos.x + " " + pos.y + " " + radius + " " + isInfected;
    }
}
