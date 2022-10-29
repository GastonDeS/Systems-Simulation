package models;

public class Human extends Person {

    public Human(String id, double positionX, double positionY) {
        super(id, positionX, positionY, false);
    }

    @Override
    protected void update(double deltaT) {

    }
}
