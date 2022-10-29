package models;

public class Human extends Person {

    public Human(String id, double positionX, double positionY, double radius) {
        super(id, positionX, positionY, radius, false);
    }

    @Override
    protected void update(double deltaT) {

    }
}
