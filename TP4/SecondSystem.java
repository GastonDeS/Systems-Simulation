import utils.Config;
import utils.Pair;
import utils.Particle;
import utils.SimulationType;
import utils.algorithms.Algorithm;
import utils.algorithms.EulerAlgorithm;
import utils.algorithms.VerletOriginalAlgorithm;
import utils.missions.AbstractMission;
import utils.missions.EarthMission;
import utils.missions.VenusMission;
import utils.predicates.Predicate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SecondSystem {
    private static final int DAY_IN_SECONDS = 86400;
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
    static double maxTime = 31536000 + 73690800; //31536000 + 22321400;
    static double takeOffTime = 73690800; //22321400;
    static SimulationType simulationType = SimulationType.OPTIMIZE_SPEED;
    static AbstractMission.MissionTarget missionTarget = AbstractMission.MissionTarget.EARTH;
    static Particle earth = getInitialValues(EARTH_COND_FILE, EARTH_RADIUS, EARTH_MASS);
    static Particle venus = getInitialValues(VENUS_COND_FILE, VENUS_RADIUS, VENUS_MASS);
    static double spaceshipInitialSpeed = 8.; // km/s

    public static void main(String[] args) {
        switch (simulationType) {
            case MAIN:
                mainSimulation();
                break;
            case OPTIMUM_DATE:
                getOptimumDate();
                break;
            case DELTA_T:
                calculateDeltaT();
                break;
            case TIME_AND_SPEED:
                calculateTimeAndSpeed();
                break;
            case MIN_DISTANCE:
                getMinDistances(0, (int) maxTime, DAY_IN_SECONDS);
                break;
            case OPTIMIZE_SPEED:
                optimizeSpeed();
                break;
            default:
                break;
        }
    }

    private static AbstractMission mainSimulation() {
        Config config = new Config()
                .withDeltaT(deltaT)
                .withSteps(steps)
                .withMaxTime(maxTime)
                .withTakeOffTime(takeOffTime)
                .withInitialSpeed(spaceshipInitialSpeed);
        Algorithm algorithm = new VerletOriginalAlgorithm(new EulerAlgorithm(K, gamma), K, gamma);

        AbstractMission mission;
        if (missionTarget == AbstractMission.MissionTarget.VENUS) {
            mission = new VenusMission(earth, venus, algorithm, config);
        } else {
            mission = new EarthMission(venus, earth, algorithm, config);
        }
        mission.simulate(simulationType);
        return mission;
    }

    private static void calculateTimeAndSpeed() {
        AbstractMission venusMission = mainSimulation();
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
            AbstractMission mission = mainSimulation();
            List<Pair<Double, Double>> timeAndEnergy = mission.getTimeAndEnergy();
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
        int interval = (int) deltaT;
        for(long t = (278*86400)-86400; t <= (278*86400) + 86400; t += interval) {
            System.out.println(t);
            takeOffTime = t;
            AbstractMission mission = mainSimulation();
            Predicate result = mission.getResult();
            if (result.getState() == Predicate.State.LANDED) {
                System.out.println("LANDED ON: " + t);
                return;
            }
        }
    }

    private static void getMinDistances(int start, int end, int step) {
        List<Pair<Double, Double>> distances = new ArrayList<>();
        for(long t = start; t <= end; t += step){
            takeOffTime = t;
            AbstractMission mission = mainSimulation();
            Double minDistance = mission.getMinDistance() - VENUS_RADIUS;
            distances.add(new Pair<>(takeOffTime, minDistance));
        }
        saveMinDistance(distances, step);
    }

    private static void optimizeSpeed() {
        List<Pair<Double,Double>> data = new ArrayList<>();
        int j = 0;
        double step = 0.0001;
        double v0 = 7.9;
        for (double i = v0; i < 8.1; i+=step, j++) {
            spaceshipInitialSpeed = i;
            AbstractMission mission = mainSimulation();
            Predicate result = mission.getResult();
            if (result.getState() == Predicate.State.LANDED) {
                data.add(new Pair<>(mission.getCurrentTime(), i));
            }
        }
        saveSpeeds(data, v0, step);
    }

    private static void saveSpeeds(List<Pair<Double, Double>> speeds, double v0, double step) {
        File smallLads = new File("TP4/timevsspeed/v0=" + v0 + "&S=" + step + ".txt");
        writeFile(smallLads, speeds, "timevsspeed");
    }

    private static void saveEnergy(List<Pair<Double, Double>> timeAndEnergy) {
        File smallLads = new File("TP4/energy/energy" + deltaT + ".txt");
        writeFile(smallLads, timeAndEnergy, "energy");
    }

    private static void saveTimeAndSpeed(List<Pair<Double, Double>> speedAndTime) {
        File smallLads = new File("TP4/speed/speed.txt");
        writeFile(smallLads, speedAndTime, "speed");
    }

    private static void saveMinDistance(List<Pair<Double, Double>> distances, int steps) {
        File smallLads = new File("TP4/distance/distance" + steps + ".txt");
        writeFile(smallLads, distances, "distance");
    }

    private static void writeFile(File file, List<Pair<Double, Double>> data, String folder) {
        try {
            FileWriter smallLadsFile = new FileWriter(file);
            for (Pair<Double, Double> d : data) {
                System.out.println(d.getKey()/DAY_IN_SECONDS + " " + d.getValue());
                smallLadsFile.write(d.getKey()/DAY_IN_SECONDS + " " + d.getValue() + "\n");
            }
            smallLadsFile.close();
        } catch (IOException ex) {
            System.out.println("Add folder " + folder + " to TP4");
        }
    }
}
