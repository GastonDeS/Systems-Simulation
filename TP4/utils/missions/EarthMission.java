package utils.missions;

import utils.Config;
import utils.Particle;
import utils.algorithms.Algorithm;
import utils.predicates.EnteredOrbitPredicate;
import utils.predicates.MaxTimePredicate;
import utils.predicates.MissedTargetPredicate;

import java.awt.geom.Point2D;
import java.util.List;

public class EarthMission extends AbstractMission{

    public EarthMission(Particle venus, Particle earth, Algorithm algorithm, Config config) {
        super(venus, earth, algorithm, config);
        this.predicates.add(new MaxTimePredicate(config.getMaxTime(), config.getDeltaT()));
        this.predicates.add(new MissedTargetPredicate(this.target, MissionTarget.EARTH));
        this.predicates.add(new EnteredOrbitPredicate(this.sun));
    }

    @Override
    protected void positionShip(List<Particle> planets) {
        System.out.println("LAUNCHING SPACESHIP");
        double sunEarthDist = origin.distance(sun);
        Point2D.Double normalComponents = getNormalComponents(origin, sun, sunEarthDist);
        this.spaceship = new Particle()
                .withLabel(SPACESHIP_ID)
                .withRadius(0.01)
                .withMass(2e5)
                .withPosX(this.origin.getPosX() + (stationDistanceToEarthSurface + origin.getRadius()) * normalComponents.x)
                .withPosY(this.origin.getPosY() + (stationDistanceToEarthSurface + origin.getRadius()) * normalComponents.y)
                .withAccX(0)
                .withAccY(0);

        double spaceshipEarthDist = spaceship.distance(origin);
        Point2D.Double tangentialComponents = getTangentialComponents(spaceship, origin, spaceshipEarthDist);
        this.spaceship.setVelX(origin.getVelX() + Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.x);
        this.spaceship.setVelY(origin.getVelY() + Math.abs(stationSpeedToEarth + spaceshipInitialSpeed) * tangentialComponents.y);

        setAcceleration(this.spaceship, planets);
    }
}
