package utils.missions;

import utils.Config;
import utils.Particle;
import utils.algorithms.Algorithm;
import utils.predicates.EnteredOrbitPredicate;
import utils.predicates.MaxTimePredicate;
import utils.predicates.MissedTargetPredicate;

import java.awt.geom.Point2D;
import java.util.List;

public class MarsMission extends AbstractMission{

    public MarsMission(Particle earth, Particle mars, Algorithm algorithm, Config config) {
        super(earth, mars, algorithm, config);
        this.predicates.add(new MaxTimePredicate(config.getMaxTime(), config.getDeltaT()));
        this.predicates.add(new MissedTargetPredicate(this.target, MissionTarget.MARS));
        this.predicates.add(new EnteredOrbitPredicate(this.sun, MissionTarget.MARS));
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
}
