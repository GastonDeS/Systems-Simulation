import javafx.util.Pair;
import utils.Config;
import utils.Particle;
import utils.VenusMission;
import utils.algorithms.Algorithm;
import utils.algorithms.EulerAlgorithm;
import utils.algorithms.VerletOriginalAlgorithm;
import utils.predicates.Predicate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    static double deltaT = 300;
    static double maxTime = 378432000;
    static double takeOffTime = 22321400;

    public static void main(String[] args) {
        //mainSimulation();
        //getOptimumDate(); // Ejercicio 1a
        //calculateDeltaT();
        //calculateTimeAndSpeed();
        getMinDistances();
    }

    private static VenusMission mainSimulation() {
        Particle earth = getInitialValues(EARTH_COND_FILE, EARTH_RADIUS, EARTH_MASS);
        Particle venus = getInitialValues(VENUS_COND_FILE, VENUS_RADIUS, VENUS_MASS);

        Config config = new Config().withDeltaT(deltaT).withSteps(steps).withMaxTime(maxTime).withTakeOffTime(takeOffTime);
        Algorithm algorithm = new VerletOriginalAlgorithm(new EulerAlgorithm(K, gamma), K, gamma);

        VenusMission venusMission = new VenusMission(earth, venus, algorithm, config);
        venusMission.simulate();
        return venusMission;
    }

    private static void calculateTimeAndSpeed() {
        VenusMission venusMission = mainSimulation();
        saveTimeAndSpeed(venusMission.getTimeAndSpeed());
    }

    private static void calculateDeltaT() {
        double maxDeltaT = 60 * 60;
        steps = 5 * 60;
        List<Double> deltaTs = new ArrayList<>();
        int k = 0;
        for(double i = steps; i <= maxDeltaT;i+= steps){
            if(k % 3 == 0 || i == maxDeltaT){
                deltaTs.add(i);
            }
            k++;
        }

        for(Double dt : deltaTs) {
            deltaT = dt;
            System.out.println("dT: " + deltaT);
            VenusMission venusMission = mainSimulation();
            List<Pair<Double, Double>> timeAndEnergy = venusMission.getTimeAndEnergy();
            saveEnergy(timeAndEnergy);
        }
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

    private static void getOptimumDate() {
        Particle earth = getInitialValues(EARTH_COND_FILE, EARTH_RADIUS, EARTH_MASS);
        Particle venus = getInitialValues(VENUS_COND_FILE, VENUS_RADIUS, VENUS_MASS);
        int interval = (int) deltaT;
        for(long t = 22100000; t <= maxTime; t += interval){
            takeOffTime = t;
            Config config = new Config().withDeltaT(deltaT).withSteps(steps).withMaxTime(maxTime).withTakeOffTime(takeOffTime);
            Algorithm algorithm = new VerletOriginalAlgorithm(new EulerAlgorithm(K, gamma), K, gamma);

            VenusMission venusMission = new VenusMission(earth, venus, algorithm, config);
            venusMission.simulate();
            Predicate result = venusMission.getResult();
            if (result.getState() == Predicate.State.LANDED) {
                System.out.println("LANDED: " + t);
                return;
            }
        }
    }

    private static void getMinDistances() {
        List<Pair<Double, Double>> distances = new ArrayList<>();
        for(long t = 0; t <= takeOffTime + 10000000; t += (3600*24)){
            takeOffTime = t;
            VenusMission venusMission = mainSimulation();
            Double minDistance = venusMission.getMinDistance();
            distances.add(new Pair<>(takeOffTime, minDistance));
        }
        saveMinDistance(distances);
    }

    private static void saveEnergy(List<Pair<Double, Double>> timeAndEnergy) {
        File smallLads = new File("TP4/energy/energy" + deltaT + ".txt");
        writeFile(smallLads, timeAndEnergy, "energy");
    }

    private static void saveTimeAndSpeed(List<Pair<Double, Double>> speedAndTime) {
        File smallLads = new File("TP4/speed/speed.txt");
        writeFile(smallLads, speedAndTime, "speed");
    }

    private static void saveMinDistance(List<Pair<Double, Double>> distances) {
        File smallLads = new File("TP4/distance/distance.txt");
        writeFile(smallLads, distances, "distance");
    }

    private static void writeFile(File file, List<Pair<Double, Double>> data, String folder) {
        try {
            FileWriter smallLadsFile = new FileWriter(file);
            for (Pair<Double, Double> d : data) {
                System.out.println(d.getKey()/86400 + " " + d.getValue());
                smallLadsFile.write(d.getKey()/86400 + " " + d.getValue() + "\n");
            }
            smallLadsFile.close();
        } catch (IOException ex) {
            System.out.println("Add folder " + folder + " to TP4");
        }
    }
}
