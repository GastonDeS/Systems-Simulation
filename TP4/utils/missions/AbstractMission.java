package utils.missions;

import utils.Config;
import utils.Pair;
import utils.Particle;
import utils.SimulationType;
import utils.algorithms.Algorithm;
import utils.predicates.Predicate;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractMission {
    private static final int SUN_ID = 0;
    private static final int ORIGIN_ID = 1;
    private static final int TARGET_ID = 2;
    private final static double G = 6.693e-20; // km^3/kg/s^2
    protected static final int SPACESHIP_ID = 3;
    protected final static double stationDistanceToEarthSurface = 1500; // km
    protected final static double stationSpeedToEarth = 7.12; // km/s

    protected final Particle sun;
    protected Particle origin;
    protected Particle target;
    protected Particle spaceship;
    private final Algorithm algorithm;
    private boolean hasTakenOff;
    protected final Config config;
    private Predicate result;

    private int iter;

    protected final List<Predicate> predicates = new ArrayList<>();
    private List<Pair<Double, Double>> timeAndEnergy = new ArrayList<>();
    private List<Pair<Double, Double>> timeAndSpeed = new ArrayList<>();
    private double initialEnergy = 0.;
    private double minDistance = Double.MAX_VALUE;
    private double currentTime;

    public AbstractMission(Particle origin, Particle target, Algorithm algorithm, Config config) {
        this.origin = origin.withLabel(ORIGIN_ID);
        this.target = target.withLabel(TARGET_ID);
        this.sun = new Particle(SUN_ID, 1.989e30, 15000, 0, 0, 0, 0, 0, 0);
        setAcceleration(target, Arrays.asList(origin, sun));
        setAcceleration(origin, Arrays.asList(target, sun));
        this.algorithm = algorithm;
        this.hasTakenOff = false;
        this.config = config;
    }

    public void simulate(SimulationType simulationType, int iter) {
        this.currentTime = 0;
        Particle pastOrigin = null;
        Particle pastTarget = null;
        Particle pastSpaceship = null;
        Particle futureOrigin;
        Particle futureTarget;
        Particle futureSpaceship;
        initialEnergy = calculateEnergy(Arrays.asList(origin, target, sun), 0);

        while (!cut()) {
            if (!hasTakenOff && currentTime >= config.getTakeOffTime()) {
                positionShip(Arrays.asList(origin, sun, target));
                hasTakenOff = true;
                minDistance = Double.min(minDistance, target.distanceRadius(spaceship));
            }

            futureOrigin = algorithm.update(pastOrigin, origin, config.getDeltaT(), currentTime);
            futureTarget = algorithm.update(pastTarget, target, config.getDeltaT(), currentTime);
            pastOrigin = origin;
            pastTarget = target;
            origin = futureOrigin;
            target = futureTarget;
            setAcceleration(origin, Arrays.asList(target, sun));
            setAcceleration(target, Arrays.asList(origin, sun));

            if (hasTakenOff) {
                futureSpaceship = algorithm.update(pastSpaceship, spaceship, config.getDeltaT(), currentTime);
                pastSpaceship = spaceship;
                spaceship = futureSpaceship;
                setAcceleration(spaceship, Arrays.asList(origin, target, sun));
                minDistance = Double.min(minDistance, target.distanceRadius(spaceship));
            }

            if (iter % config.getSteps() == 0) {
                switch (simulationType) {
                    case MAIN:
                    case ROUND_TRIP:
                        saveState(iter);
                        break;
                    case DELTA_T:
                        if(hasTakenOff) {
                            calculateEnergy(Arrays.asList(origin, target, sun, spaceship), currentTime);
                        } else {
                            calculateEnergy(Arrays.asList(origin, sun, target), currentTime);
                        }
                        break;
                    case TIME_AND_SPEED:
                        if (hasTakenOff) {
                            saveSpeedAndTime(spaceship, currentTime);
                        }
                        break;
                    default:
                        break;
                }
            }

            iter++;
            this.iter = iter;
            currentTime += config.getDeltaT();
        }
    }

    protected abstract void positionShip(List<Particle> planets);

    private void saveSpeedAndTime(Particle spaceship, double currentTime) {
        Double spaceshipSpeed = Math.pow(Math.pow(spaceship.getVelY(), 2) + Math.pow(spaceship.getVelX(), 2), 0.5);
        timeAndSpeed.add(new Pair<>(currentTime - config.getTakeOffTime(), spaceshipSpeed));
    }

    private boolean cut() {
        for (Predicate predicate : predicates) {
            if (predicate.predict(spaceship, target)) {
                predicate.print();
                result = predicate;
                return true;
            }
        }
        return false;
    }

    protected void setAcceleration(Particle target, List<Particle> interactions) {
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

    protected Point2D.Double getNormalComponents(Particle pi, Particle pj, double dist) {
        return new Point2D.Double((pj.getPosX() - pi.getPosX())/dist,(pj.getPosY() - pi.getPosY())/dist);
    }

    protected Point2D.Double getTangentialComponents(Particle pi, Particle pj, double dist) {
        Point2D.Double normalComponents = getNormalComponents(pi, pj, dist);
        return new Point2D.Double(-normalComponents.y, normalComponents.x);
    }

    private double getGravitationalForce(Particle pi, Particle pj, double dist){
        return G * pi.getMass() * pj.getMass()/Math.pow(dist,2);
    }

    private void saveState(int iter) {
        File smallLads = new File("position/positions" + iter + ".xyz");
        try {
            FileWriter smallLadsFile = new FileWriter(smallLads);
            int quantity = hasTakenOff ? 4 : 3;
            smallLadsFile.write(
                    quantity + "\n" +
                            "Lattice=\"6 0.0 0.0 0.0 6 0.0 0.0 0.0 6\"" +
                            "\n");
            smallLadsFile.write(sun + "\n");
            smallLadsFile.write(origin + "\n");
            smallLadsFile.write(target + "\n");

            if (hasTakenOff) {
                Particle spaceshipv2 = spaceship.clone();
                spaceshipv2.withRadius(spaceshipv2.getRadius()* 200000);
                smallLadsFile.write(spaceshipv2 + "\n");
            }

            smallLadsFile.close();
        } catch (IOException ex) {
            System.out.println("Add folder position to TP4");
        }
    }

    public List<Pair<Double, Double>> getTimeAndEnergy() {
        return timeAndEnergy;
    }

    public List<Pair<Double, Double>> getTimeAndSpeed() {
        return timeAndSpeed;
    }

    public Predicate getResult() {
        return result;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public Particle getOrigin() {
        return origin;
    }

    public Particle getTarget() {
        return target;
    }

    public enum MissionTarget {
        VENUS,
        EARTH
    }

    public int getIter() {
        return iter;
    }
}
