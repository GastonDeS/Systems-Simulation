import utils.Oscillator;
import utils.algorithms.*;
import utils.UpdateMethod;

public class FirstSystem {

    static double m = 70.;
    static double K = Math.pow(10,4);
    static double gamma = 100.;
    static int A = 1;
    // TODO: read from config.json
    static int tf = 5;
    static UpdateMethod updateMethod = UpdateMethod.EULER_MODIFIED;
    static int steps = 2;
    static double deltaT = 0.01;

    public static void main(String[] args) {
        System.out.println("TP4");
        Algorithm algorithm;

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
//                algorithm = VerletAlgorithm(K, gamma);
//                break;
            default:
                throw new IllegalArgumentException("No such algorithm found");
        }

        Oscillator oscillator = new Oscillator(algorithm, tf, steps);
        oscillator.simulate(deltaT);

//        double initialSpeed = -A*gamma/(2*m);
//        int initialPosition = A;
//        double initialForce = -K*initialPosition - gamma*initialSpeed;
//        Particle particle = new Particle('-', 1, 0, initialPosition, 0,
//                initialSpeed, 0, initialForce, 0);
//        for(double t = 0 ; t < tf ; t+=0.05){
//            double r = A*Math.exp(-gamma*t/(2*m))*Math.cos(Math.sqrt(K/m - Math.pow(gamma/(2*m), 2))*t);
//            System.out.println(r);
//        }
    }
}
