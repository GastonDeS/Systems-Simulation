package utils.algorithms;

import utils.UpdateMethod;

public abstract class AlgorithmImpl implements Algorithm {

    protected final double K;
    protected final double gamma;
    protected UpdateMethod updateMethod;

    public AlgorithmImpl(double K, double gamma, UpdateMethod updateMethod) {
        this.K = K;
        this.gamma = gamma;
        this.updateMethod = updateMethod;
    }

    @Override
    public String getName() {
        return updateMethod.name;
    }
}
