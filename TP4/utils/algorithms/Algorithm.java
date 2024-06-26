package utils.algorithms;

import utils.Particle;

public interface Algorithm {
    Particle update(Particle previous, Particle current, double deltaT, double currTime);

    String getName();
}
