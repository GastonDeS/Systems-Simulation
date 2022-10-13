package utils.missions;

import utils.Config;
import utils.Particle;
import utils.algorithms.Algorithm;
import utils.missions.AbstractMission;
import utils.predicates.EnteredOrbitPredicate;
import utils.predicates.MaxTimePredicate;
import utils.predicates.MissedTargetPredicate;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Initial conditions for a planet at a given time step respective to the sun
 * Velocity is in km/s
 * Distance is in km
 */
public class VenusMission extends AbstractMission {

    public VenusMission(Particle earth, Particle venus, Algorithm algorithm, Config config) {
        super(earth, venus, algorithm, config);
        this.predicates.add(new MaxTimePredicate(config.getMaxTime(), config.getDeltaT()));
        this.predicates.add(new MissedTargetPredicate(this.target, MissionTarget.VENUS));
        this.predicates.add(new EnteredOrbitPredicate(this.sun));
    }

    @Override
    protected void positionShip(List<Particle> planets) {
        System.out.println("LAUNCHING SPACESHIP");
        double sunEarthDist = earth.distance(sun);
        Point2D.Double normalComponents = getNormalComponents(earth, sun, sunEarthDist);
        this.spaceship = new Particle()
                .withLabel(SPACESHIP_ID)
                .withRadius(0.01)
                .withMass(2e5)
                .withPosX(this.earth.getPosX() - (stationDistanceToEarthSurface + earth.getRadius()) * normalComponents.x)
                .withPosY(this.earth.getPosY() - (stationDistanceToEarthSurface + earth.getRadius()) * normalComponents.y)
                .withAccX(0)
                .withAccY(0);

        double spaceshipEarthDist = spaceship.distance(earth);
        Point2D.Double tangentialComponents = getTangentialComponents(spaceship, earth, spaceshipEarthDist);
        this.spaceship.setVelX(earth.getVelX() + Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.x);
        this.spaceship.setVelY(earth.getVelY() + Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.y);

        setAcceleration(this.spaceship, planets);
    }
}
