package models;

public class Zombie extends Person {

    public Zombie(String id, double speed, double angle, double positionX, double positionY) {
        super(id, speed, angle, positionX, positionY, true);
    }
}