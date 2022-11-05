import models.Config;
import models.FileLog;
import models.Room;

public class ZombieEpidemic {
    private static final Config config                = setConfig();
    private static final Variable variable            = Variable.NO_VARIABLE;
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
        room.savePersons(0);
        int prevNz = 0;

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
                        System.out.println(room.getZombieCount() + " " + prevNz);
                        double vel = (double) (room.getZombieCount() - prevNz) / config.getSteps();
                        prevNz = room.getZombieCount();
                        fileLog.write(t + " " + vel + "\n");
                        break;
                }
            }

            t += deltaT;
            iter++;
        }

        if (fileLog != null) {
            fileLog.close();
        }
    }

    private static void simulationWithNhVariable() {
        config.withVdz(0.3);
        int[] Nhs = new int[]{2, 10, 40, 80, 140, 200, 260, 320, 380};
        for (int Nh : Nhs) {
            config.withNh(Nh);
            for (int i = 0; i < 10; i++) {
                String fileName = simulationType == SimulationType.INFECTION_SPEED ? "speeds/speedNh" : "ratios/ratioNh";
                FileLog fileLog = new FileLog("TP5/" + fileName + Nh + "_" + i + ".txt");
                mainSimulation(fileLog);
            }
        }
    }

    private static void simulationWithVdzVariable() {
        config.withNh(200);
        double[] Vdzs = new double[]{1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5};
        for (double Vdz : Vdzs) {
            config.withVdz(Vdz);
            for (int i = 0; i < 10; i++) {
                String fileName = simulationType == SimulationType.INFECTION_SPEED ? "speeds/speedVdz" : "ratios/ratioVdz";
                FileLog fileLog = new FileLog("TP5/" + fileName + Vdz + "_" + i + ".txt");
                mainSimulation(fileLog);
            }
        }
    }

    private static Config setConfig() {
        return new Config()
                .withNh(100)
                .withDeltaT(0.0125)
                .withVdz(3)
                .withTau(0.5)
                .withApHuman(1)
                .withBpHuman(500)
                .withApZombie(5)
                .withBpZombie(1000)
                .withApWall(4)
                .withBpWall(1000)
                .withMaxTime(300)
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
        MAIN
    }
}
