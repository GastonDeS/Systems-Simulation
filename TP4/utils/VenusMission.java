package utils;

import utils.algorithms.Algorithm;
import utils.algorithms.VerletOriginalAlgorithm;
import utils.predicates.MaxTimePredicate;
import utils.predicates.MissedTargetPredicate;
import utils.predicates.Predicate;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Initial conditions for a planet at a given time step respective to the sun
 * Velocity is in km/s
 * Distance is in km
 */
public class VenusMission {
    private static final int SUN_ID = 0;
    private static final int EARTH_ID = 1;
    private static final int VENUS_ID = 2;
    private static final int SPACESHIP_ID = 3;
    private final static double G = 6.693e-20; // km^3/kg/s^2
    private final static double stationDistanceToEarthSurface = 1500; // km
    private final static double stationSpeedToEarth = 7.12; // km/s
    private final static double spaceshipInitialSpeed = 8.; // km/s

    private final Particle sun;
    private Particle earth;
    private Particle venus;
    private Particle spaceship;
    private final Algorithm algorithm;
    private boolean hasTakenOff;
    private final Config config;
    private final List<Predicate> predicates = new ArrayList<>();

    public VenusMission(Particle earth, Particle venus, Algorithm algorithm, Config config) {
        this.earth = earth.withLabel(EARTH_ID);
        this.venus = venus.withLabel(VENUS_ID);
        this.sun = new Particle(SUN_ID, 1.989e30, 15000, 0, 0, 0, 0, 0, 0);
        setAcceleration(venus, Arrays.asList(earth, sun));
        setAcceleration(earth, Arrays.asList(venus, sun));
        this.algorithm = algorithm;
        this.hasTakenOff = false;
        this.config = config;
        this.predicates.add(new MaxTimePredicate(config.getMaxTime(), config.getDeltaT()));
        this.predicates.add(new MissedTargetPredicate(this.venus));
    }

    public void simulate() {
        double currentTime = 0;
        Particle pastEarth = null;
        Particle pastVenus = null;
        Particle pastSpaceship = null;
        Particle futureEarth;
        Particle futureVenus;
        Particle futureSpaceship;
        int iter = 0;

        while (!cut(spaceship)) {

            if (!hasTakenOff && currentTime >= config.getTakeOffTime()) {
                positionShip(Arrays.asList(earth, sun, venus));
                hasTakenOff = true;
            }

            futureEarth = algorithm.update(pastEarth, earth, config.getDeltaT(), currentTime);
            futureVenus = algorithm.update(pastVenus, venus, config.getDeltaT(), currentTime);
            pastEarth = earth;
            pastVenus = venus;
            earth = futureEarth;
            venus = futureVenus;
            setAcceleration(earth, Arrays.asList(venus, sun));
            setAcceleration(venus, Arrays.asList(earth, sun));

            if (hasTakenOff) {
                futureSpaceship = algorithm.update(pastSpaceship, spaceship, config.getDeltaT(), currentTime);
                pastSpaceship = spaceship;
                spaceship = futureSpaceship;
                setAcceleration(spaceship, Arrays.asList(earth, venus, sun));
            }

            if (iter % config.getSteps() == 0) {
                saveState(iter);
            }

            iter++;
            currentTime += config.getDeltaT();
        }
    }

    private boolean cut(Particle spaceship) {
        for (Predicate predicate : predicates) {
            return predicate.predict(spaceship);
        }
        return false;
    }

    private void setAcceleration(Particle target, List<Particle> interactions) {
        double Fx = 0;
        double Fy = 0;
        for(Particle p : interactions){
            double dist = target.distance(p);
            double fn = getGravitationalForce(target, p, dist);
            Point2D.Double normalComponents = getNormalComponents(target, p, dist);
            Fx += fn * normalComponents.x;
            Fx += fn * normalComponents.y;
        }
        target.setAccX(Fx/target.getMass());
        target.setAccY(Fy/target.getMass());
    }

    private Point2D.Double getNormalComponents(Particle pi, Particle pj, double dist) {
        return new Point2D.Double((pj.getPosX() - pi.getPosX())/dist,(pj.getPosY() - pi.getPosY())/dist);
    }

    private Point2D.Double getTangentialComponents(Particle pi, Particle pj, double dist) {
        Point2D.Double normalComponents = getNormalComponents(pi, pj, dist);
        return new Point2D.Double(-normalComponents.y, normalComponents.x);
    }

    private double getGravitationalForce(Particle pi, Particle pj, double dist){
        return G * pi.getMass() * pj.getMass()/Math.pow(dist,2);
    }

    private void positionShip(List<Particle> planets){
        System.out.println("LAUNCHING SPACESHIP");
        double sunEarthDist = earth.distance(sun);
        Point2D.Double normalComponents = getNormalComponents(earth, sun, sunEarthDist);
        this.spaceship = new Particle()
                .withLabel(SPACESHIP_ID)
                .withRadius(2000)
                .withMass(2e5)
                .withPosX(this.earth.getPosX() + (stationDistanceToEarthSurface + earth.getRadius()) * normalComponents.x)
                .withPosY(this.earth.getPosY() + (stationDistanceToEarthSurface + earth.getRadius()) * normalComponents.y)
                .withAccX(0)
                .withAccY(0);

        double spaceshipEarthDist = spaceship.distance(earth);
        Point2D.Double tangentialComponents = getTangentialComponents(spaceship, earth, spaceshipEarthDist);
        this.spaceship.setVelX(earth.getVelX() + Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.x);
        this.spaceship.setVelY(earth.getVelY() + Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.y);

        setAcceleration(this.spaceship, planets);
    }

    private void saveState(int iter) {
        File smallLads = new File("TP4/position/positions" + iter + ".xyz");
        try {
            FileWriter smallLadsFile = new FileWriter(smallLads);
            int quantity = hasTakenOff ? 4 : 3;
            smallLadsFile.write(
                    quantity + "\n" +
                            "Lattice=\"6 0.0 0.0 0.0 6 0.0 0.0 0.0 6\"" +
                            "\n");
            smallLadsFile.write(sun + "\n");
            smallLadsFile.write(earth + "\n");
            smallLadsFile.write(venus + "\n");
            if (hasTakenOff) smallLadsFile.write(spaceship + "\n");

            smallLadsFile.close();
        } catch (IOException ex) {
            System.out.println("Add folder position to TP4");
        }
    }
}
