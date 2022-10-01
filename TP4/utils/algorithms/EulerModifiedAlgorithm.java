package utils.algorithms;

import utils.Particle;
import utils.UpdateMethod;

public class EulerModifiedAlgorithm extends EulerAlgorithm {
    public EulerModifiedAlgorithm(double K, double gamma) {
        super(K, gamma, UpdateMethod.EULER_MODIFIED);
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT) {
        if (current == null) throw new NullPointerException("No current particle to update with");
        Particle next = current.clone();
        super.updateVel(current, next, deltaT);
        super.updatePos(current, next, deltaT, true);
        super.updateForce(next);
        return next;
    }

}
