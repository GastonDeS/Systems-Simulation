import utils.Config;
import utils.Particle;
import utils.VenusMission;
import utils.algorithms.Algorithm;
import utils.algorithms.EulerAlgorithm;
import utils.algorithms.VerletOriginalAlgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SecondSystem {
    private static final double EARTH_RADIUS = 6371;
    private static final double VENUS_RADIUS = 6051.8;
    private static final double EARTH_MASS = 5.97219e24;
    private static final double VENUS_MASS = 4.867e24;
    private static final String EARTH_COND_FILE = "TP4/nasaData/earth.csv";
    private static final String VENUS_COND_FILE = "TP4/nasaData/venus.csv";

    static double K = Math.pow(10,4);
    static double gamma = 100.0;
    static int steps = 72;
    static double deltaT = 3000;
    static double maxTime = 378432000;
    static double takeOffTime = 31801800;

    public static void main(String[] args) {
        Particle earth = getInitialValues(EARTH_COND_FILE, EARTH_RADIUS, EARTH_MASS);
        Particle venus = getInitialValues(VENUS_COND_FILE, VENUS_RADIUS, VENUS_MASS);

        Config config = new Config().withDeltaT(deltaT).withSteps(steps).withMaxTime(maxTime).withTakeOffTime(takeOffTime);
        Algorithm algorithm = new VerletOriginalAlgorithm(new EulerAlgorithm(K, gamma), K, gamma);

        VenusMission venusMission = new VenusMission(earth, venus, algorithm, config);
        venusMission.simulate();
    }

    private static Particle getInitialValues(String filename, double radius, double mass) {
        File initConditions = new File(filename);
        Particle particle = new Particle();
        try (FileReader fileReader = new FileReader(initConditions)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            String[] values = line.split(",");
            particle = new Particle()
                    .withMass(mass)
                    .withRadius(radius)
                    .withPosX(Double.parseDouble(values[1]))
                    .withPosY(Double.parseDouble(values[2]))
                    .withVelX(Double.parseDouble(values[3]))
                    .withVelY(Double.parseDouble(values[4]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return particle;
    }
}
