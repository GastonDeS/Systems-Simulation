package models;

public class Zombie extends Person {

    public Zombie(String id, double positionX, double positionY) {
        super(id, positionX, positionY, true);
    }

    @Override
    protected void update(double deltaT) {

    }
}