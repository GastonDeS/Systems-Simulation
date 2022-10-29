package models;

import java.awt.geom.Point2D;

public abstract class Person {
    private final String id;
    private Point2D.Double vel;
    private Point2D.Double pos;
    private final double radius;
    private boolean isInfected;

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

    protected abstract void update(double deltaT);

    @Override
    public String toString() {
        return id + " " + pos.x + " " + pos.y + " " + radius + " " + isInfected;
    }
}
