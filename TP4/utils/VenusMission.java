package utils;

import javafx.util.Pair;
import utils.algorithms.Algorithm;
import utils.algorithms.VerletOriginalAlgorithm;
import utils.predicates.EnteredOrbitPredicate;
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
    private Predicate result;
    private List<Pair<Double, Double>> timeAndEnergy = new ArrayList<>();
    double initialEnergy = 0.;

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
        this.predicates.add(new EnteredOrbitPredicate(this.sun));
    }

    public Predicate simulate() {
        double currentTime = 0;
        double targetMinDistance = Double.MAX_VALUE;
        Particle pastEarth = null;
        Particle pastVenus = null;
        Particle pastSpaceship = null;
        Particle futureEarth;
        Particle futureVenus;
        Particle futureSpaceship;
        int iter = 0;
        initialEnergy = calculateEnergy(Arrays.asList(earth, venus, sun), 0);

        while (!cut()) {
            if (!hasTakenOff && currentTime >= config.getTakeOffTime()) {
                positionShip(Arrays.asList(earth, sun, venus));
                hasTakenOff = true;
                targetMinDistance = Double.min(targetMinDistance, venus.distance(spaceship));
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
                targetMinDistance = Double.min(targetMinDistance, venus.distance(spaceship));
            }

            if (iter % config.getSteps() == 0) {
                //saveState(iter);
                if(hasTakenOff)
                    calculateEnergy(Arrays.asList(earth, venus, sun, spaceship), currentTime);
                else
                    calculateEnergy(Arrays.asList(earth, sun , venus),currentTime);
            }

            iter++;
            currentTime += config.getDeltaT();
        }
        return result;
    }

    private boolean cut() {
        for (Predicate predicate : predicates) {
            if (predicate.predict(spaceship, venus)) {
                predicate.print();
                result = predicate;
                return true;
            }
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
            Fy += fn * normalComponents.y;
        }
        target.setAccX(Fx/target.getMass());
        target.setAccY(Fy/target.getMass());
    }

    private double calculateEnergy(List<Particle> particles, double currentTime) {
        double K = 0;
        double P = 0;
        for(int i = 0 ; i < particles.size(); i++){
            Particle p1 = particles.get(i);
            K += 0.5 * p1.getMass() * (Math.pow(p1.getVelX(),2) + Math.pow(p1.getVelY(),2));
            for(int j = i+1; j < particles.size(); j++){
                Particle p2 = particles.get(j);
                P += - G * p1.getMass() * p2.getMass() / p1.distance(p2);
            }
        }
        double sum = K + P;
        if (initialEnergy != 0)
            timeAndEnergy.add(new Pair<>(currentTime, Math.abs(sum - initialEnergy)));
        return sum;
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
        this.spaceship.setVelX(earth.getVelX() - Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.x);
        this.spaceship.setVelY(earth.getVelY() - Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.y);

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

    public List<Pair<Double, Double>> getTimeAndEnergy() {
        return timeAndEnergy;
    }
}
