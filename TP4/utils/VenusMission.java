package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Initial conditions for a planet at a given time step respective to the sun
 * Velocity is in km/s
 * Distance is in km
 */
public class VenusMission {
    private final List<Planet> venusCond;
    private final List<Planet> earthCond;
    private final Ship ship;
    private final static double G = 6.67408e-20; // km^3/kg/s^2 // TODO check this was set by copilot
    private final static double stationDistanceToEarthSurface = 1.5; // km
    private final static double stationSpeedToEarth = 7.12; // km/s
    private final static double shipInitialSpeed = 8.; // km/s

    public VenusMission(List<Planet> venusCond, List<Planet> earthCond) {
        this.venusCond = venusCond;
        this.earthCond = earthCond;
        this.ship = positionShip();
        this.saveState();
    }

    private Ship positionShip() {
        double angle = Math.atan(earthCond.get(0).getY() / earthCond.get(0).getX()); // angle between earth and sun
        Planet earth = getEarthCond(0);
        System.out.println("displacement "+ (stationDistanceToEarthSurface + earth.getRadius()) * Math.cos(angle) );
        return new Ship(
                // TODO add earth radius
                earth.getX() - (stationDistanceToEarthSurface + earth.getRadius()) * Math.cos(angle),
                earth.getY() - (stationDistanceToEarthSurface + earth.getRadius()) * Math.sin(angle),
                // earth velocity + ship and station direction ( station + ship initial velocity)
                getEarthCond(0).getVelocityX() + Math.cos(angle - Math.PI / 2) * (stationSpeedToEarth + shipInitialSpeed),
                getEarthCond(0).getVelocityY() + Math.sin(angle - Math.PI / 2) * (stationSpeedToEarth + shipInitialSpeed),
                0,
                0);
    }

    public Planet getEarthCond(int day) {
        return earthCond.get(day);
    }

    public Planet getVenusCond(int day) {
        return venusCond.get(day);
    }

    private void saveState() {
        File smallLads = new File("TP4/position/positions.xyz");
        try {
            FileWriter smallLadsFile = new FileWriter(smallLads);

            smallLadsFile.write(
                    4+"\n" +
                            "Lattice=\"6 0.0 0.0 0.0 6 0.0 0.0 0.0 6\"" +
                            "\n");
            smallLadsFile.write("0 0 0 0 696340\n"); // sun
            smallLadsFile.write(getEarthCond(0).toString() + "\n");
            smallLadsFile.write(getVenusCond(0).toString() + "\n");
            smallLadsFile.write(ship.toString() );


            smallLadsFile.close();
        } catch (IOException ex) {
            System.out.println("Add folder position to TP4");
        }
    }
}
