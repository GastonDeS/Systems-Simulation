package models;

public class Person {
    private String id;
    private double speed;
    private double angle;
    private double positionX;
    private double positionY;
    private final static double radius = 0.15; // its fixed
    private boolean isInfected;

    public Person(String id, double speed, double angle, double positionX, double positionY, boolean isInfected) {
        this.id = id;
        this.speed = speed;
        this.angle = angle;
        this.positionX = positionX;
        this.positionY = positionY;
        this.isInfected = isInfected;
    }

    public void infect() {
        this.isInfected = true;
    }

    public boolean isContact(Person person) {
        double distance = Math.sqrt(Math.pow(this.positionX - person.positionX, 2) + Math.pow(this.positionY - person.positionY, 2));
        return distance <= 2 * radius ;
    }

    public boolean isTouchingCircularWall(double wallRadius) {
        return Math.sqrt(Math.pow(this.positionX, 2) + Math.pow(this.positionY, 2)) >= wallRadius - radius;
    }

    public static double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return id + " "+ positionX + " " + positionY + " " +radius+" "+ isInfected;
    }
}
