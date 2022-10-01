package utils.algorithms;

import utils.Particle;
import utils.UpdateMethods;

import utils.UpdateMethods;

public class EulerModifiedAlgorithm extends EulerAlgorithm {

     protected final UpdateMethods name = UpdateMethods.EULER_MODIFIED;

    public EulerModifiedAlgorithm(double K, double gamma) {
        super(K, gamma);
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

    @Override
    public String getName() {
        return name.name;
    }
}
