import models.Config;
import models.FileLog;
import models.Room;

public class ZombieEpidemic {
    private static final Config config                 = setConfig();
    private static final Variable variable             = Variable.NO_VARIABLE;
    private static final SimulationType simulationType = SimulationType.MAIN;

    public static void main(String[] args) {
        switch (variable) {
            case NH:
                simulationWithNhVariable();
                break;
            case VDZ:
                simulationWithVdzVariable();
                break;
            case NO_VARIABLE:
                mainSimulation(null);
                break;
        }
    }

    private static void mainSimulation(FileLog fileLog) {
        Room room = new Room(config);
        room.fillRoom();
        double t = 0.0;
        int iter = 1;
        double deltaT = config.getDeltaT();
        if (simulationType == SimulationType.MAIN) {
            room.savePersons(0);
        }
        int prevNz = 1;

        while (t < config.getMaxTime() && room.getHumans().size() != 0) {
            room.update(deltaT);

            if (iter % config.getSteps() == 0) {
                switch (simulationType) {
                    case MAIN:
                        room.savePersons(iter);
                        break;
                    case HUMAN_ZOMBIE_RATIO:
                        double ratio = room.getHumanZombieRatio();
                        fileLog.write(t + " " + ratio + "\n");
                        break;
                    case INFECTION_SPEED:
//                        System.out.println(room.getZombieCount() + " " + prevNz);
                        double vel = (double) (room.getZombieCount() - prevNz) / 10;
                        prevNz = room.getZombieCount();
                        fileLog.write(t + " " + vel + "\n");
                        break;
                }
            }

            t += deltaT;
            iter++;
        }

        if (simulationType == SimulationType.TIME_TO_FULL_INFECTION) {
            String data = room.getHumans().size() != 0 ? "" : Double.toString(t);
            fileLog.write(data + "\n");
        } else {
            if (fileLog != null) {
                fileLog.close();
            }
        }
    }

    private static void simulationWithNhVariable() {
        config.withVdz(3);
        int[] Nhs = new int[]{30, 60, 90, 120, 150, 180, 210, 240, 270};
        for (int Nh : Nhs) {
            config.withNh(Nh);
            if (simulationType != SimulationType.TIME_TO_FULL_INFECTION) {
                for (int i = 0; i < 10; i++) {
                    String fileName = getStringName("Nh");
                    FileLog fileLog = new FileLog("TP5/" + fileName + Nh + "_" + i + ".txt");
                    mainSimulation(fileLog);
                }
            } else {
                String fileName = getStringName("Nh");
                FileLog fileLog = new FileLog("TP5/" + fileName + Nh + ".txt");
                for (int i = 0; i < 10; i++) {
                    mainSimulation(fileLog);
                }
                fileLog.close();
            }
        }
    }

    private static void simulationWithVdzVariable() {
        config.withNh(201);
        double[] Vdzs = new double[]{1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5};
        for (double Vdz : Vdzs) {
            config.withVdz(Vdz);
            if (simulationType != SimulationType.TIME_TO_FULL_INFECTION) {
                for (int i = 0; i < 10; i++) {
                    String fileName = getStringName("Vdz");
                    FileLog fileLog = new FileLog("TP5/" + fileName + Vdz + "_" + i + ".txt");
                    mainSimulation(fileLog);
                }
            } else {
                String fileName = getStringName("Vdz");
                FileLog fileLog = new FileLog("TP5/" + fileName + Vdz + ".txt");
                for (int i = 0; i < 10; i++) {
                    mainSimulation(fileLog);
                }
                fileLog.close();
            }
        }
    }

    private static String getStringName(String variable) {
        switch (simulationType) {
            case HUMAN_ZOMBIE_RATIO:
                return "ratios/ratio" + variable;
            case INFECTION_SPEED:
                return "speeds/speed" + variable;
            case TIME_TO_FULL_INFECTION:
                return "times/time" + variable;
        }
        return null;
    }

    private static Config setConfig() {
        return new Config()
                .withNh(60)
                .withDeltaT(0.0125)
                .withVdz(3)
                .withTau(0.5)
                .withApHuman(1)
                .withBpHuman(500)
                .withApZombie(5)
                .withBpZombie(1000)
                .withApWall(2) // 2 ideal
                .withBpWall(1000)
                .withMaxTime(600)
                .withSteps(5);
    }

    private enum Variable {
        NH,
        VDZ,
        NO_VARIABLE
    }

    private enum SimulationType {
        HUMAN_ZOMBIE_RATIO,
        INFECTION_SPEED,
        TIME_TO_FULL_INFECTION,
        MAIN
    }
}
