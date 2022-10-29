package models;

public class Zombie extends Person {

    public Zombie(String id, double positionX, double positionY, double radius) {
        super(id, positionX, positionY, radius, true);
    }

    @Override
    protected void update(double deltaT) {

    }
}