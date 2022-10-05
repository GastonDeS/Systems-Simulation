package utils;

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
    private final static double stationDistanceToEarth = 1.5; // km
    private final static double stationSpeedToEarth = 7.12; // km/s
    private final static double shipInitialSpeed = 8.; // km/s

    public VenusMission(List<Planet> venusCond, List<Planet> earthCond) {
        this.venusCond = venusCond;
        this.earthCond = earthCond;
        this.ship = positionShip();
    }

    private Ship positionShip() {
        double angle = Math.atan(earthCond.get(0).getY() / earthCond.get(0).getX()); // angle between earth and sun
        Planet earth = getEarthCond(0);
        return new Ship(earth.getX() - stationDistanceToEarth * Math.cos(angle),
                earth.getY() - stationDistanceToEarth * Math.sin(angle),
                // earth velocity + ship and station direction ( station + ship initial velocity)
                getEarthCond(0).getVelocityX() + Math.cos(angle - Math.PI / 2) * (stationSpeedToEarth + shipInitialSpeed),
                getEarthCond(0).getVelocityY() + Math.sin(angle - Math.PI / 2) * (stationSpeedToEarth + shipInitialSpeed),
                0,
                0);
    }

    public Planet getEarthCond(int day) {
        return earthCond.get(day);
    }
}
