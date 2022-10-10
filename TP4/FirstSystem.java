import utils.Oscillator;
import utils.algorithms.*;
import utils.UpdateMethod;

import java.math.BigDecimal;

public class FirstSystem {

    static double m = 70.;
    static double K = Math.pow(10,4);
    static double gamma = 100.0;
    static double A = 1.0;
    // TODO: read from config.json
    static int tf = 5;
    static UpdateMethod updateMethod = UpdateMethod.ANALYTIC;

    static int steps = 2;
    static BigDecimal deltaT = BigDecimal.valueOf(0.0001);
    static BigDecimal minDeltaT = BigDecimal.valueOf(0.00001);
    static BigDecimal maxDeltaT = BigDecimal.valueOf(0.01);

    public static void main(String[] args) {
        System.out.println("TP4");
        Algorithm algorithm;

        for (BigDecimal dt = minDeltaT; dt.compareTo(maxDeltaT) <= 0; dt = dt.add(deltaT)) {
            switch (updateMethod) {
                case EULER:
                    algorithm = new EulerAlgorithm(K, gamma);
                    break;
                case EULER_MODIFIED:
                    algorithm = new EulerModifiedAlgorithm(K, gamma);
                    break;
                case GEAR_PREDICTOR_CORRECTOR:
                    algorithm = new GearPredictorCorrectorAlgorithm(K, gamma);
                    break;
                case BEEMAN:
                    algorithm = new BeemanAlgorithm(K, gamma);
                    break;
                case VERLET:
                    algorithm = new VerletOriginalAlgorithm(new EulerAlgorithm(K, gamma), K, gamma);
                    break;
                case ANALYTIC:
                    algorithm = new Analytic(K, gamma, A);
                    break;
                default:
                    throw new IllegalArgumentException("No such algorithm found");
            }

            Oscillator oscillator = new Oscillator(algorithm, tf, steps);
            System.out.println("DeltaT: " + dt);
            System.out.println("Error: " + dt.doubleValue());
            oscillator.simulate(dt.doubleValue(), gamma, K, m);
        }
    }
}
