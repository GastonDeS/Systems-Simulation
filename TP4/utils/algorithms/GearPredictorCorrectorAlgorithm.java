package utils.algorithms;

import utils.Particle;
import utils.UpdateMethod;

import java.util.stream.LongStream;

public class GearPredictorCorrectorAlgorithm extends BeemanAlgorithm implements Algorithm {
    private static final double[] ALPHAS = {3.0/20, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60};
    private double[] r;
    private double[] rp;

    public GearPredictorCorrectorAlgorithm(double K, double gamma) {
        super(K, gamma, UpdateMethod.GEAR_PREDICTOR_CORRECTOR);
        this.r = new double[] {0, 0, 0, 0, 0, 0};
        this.rp = new double[] {0, 0, 0, 0, 0, 0};
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT) {
        if (current == null) throw new NullPointerException("No current particle to update with");
        Particle next = current.clone();
        r[0] = current.getPosX();
        r[1] = current.getVelX();
        r[2] = current.getAccX();

        for (int i = 0; i < 6; i++) {
            rp[i] = getPredicted(deltaT, i);
        }

        double deltaR2 = deltaR2(deltaT, current.getMass());
        for (int i = 0; i < 6; i++) {
            r[i] = getCorrected(i, deltaR2, deltaT);
        }

        next.setPosX(r[0]);
        next.setVelX(r[1]);
        next.setAccX(r[2]);
        return next;
    }


    private double getPredicted(double deltaT, int num) {
        double ret = 0.0;
        for (int i = num; i < 6; i++) {
            ret += r[i] * Math.pow(deltaT, i - num)/factorial(i - num);
        }
        return ret;
//        switch (num) {
//            case 0:
//                return r[0] + r[1] * deltaT + r[2] * Math.pow(deltaT, 2)/2 + r[3] * Math.pow(deltaT, 3)/6
//                        + r[4] * Math.pow(deltaT, 4)/24 + r[5] * Math.pow(deltaT, 5)/120;
//            case 1:
//                return r[1] + r[2] * deltaT + r[3] * Math.pow(deltaT, 2)/2 + r[4] * Math.pow(deltaT, 3)/6
//                        + r[5] * Math.pow(deltaT, 4)/24;
//            case 2:
//                return r[2] + r[3] * deltaT + r[4] * Math.pow(deltaT, 2)/2 + r[5] * Math.pow(deltaT, 3)/6;
//            case 3:
//                return r[3] + r[4] * deltaT + r[5] * Math.pow(deltaT, 2)/2;
//            case 4:
//                return r[4] + r[5] * deltaT;
//            case 5:
//                return r[5];
//            default:
//                throw new IllegalArgumentException();
//        }
    }

    private double deltaR2(double deltaT, double mass) {
        double nextAcc = (-K * rp[0] - gamma * rp[1])/mass;
        return (nextAcc - rp[2]) * Math.pow(deltaT, 2)/2;
    }

    private double getCorrected(int num, double deltaR2, double deltaT) {
        return rp[num] + ALPHAS[num] * deltaR2 * factorial(num)/Math.pow(deltaT, num);
    }

    private double factorial(int num) {
        return (double) LongStream.rangeClosed(1, num).reduce(1, (long x, long y) -> x * y);
    }

}
