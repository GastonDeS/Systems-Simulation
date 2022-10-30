import models.Config;
import models.Room;

public class ZombieEpidemic {

    private static final int N = 30;

    public static void main(String[] args) {
        final Config config = setConfig();
        Room room = new Room(config);
        room.fillRoom();
        double t = 0.0;
        int iter = 1;
        double deltaT = config.getDeltaT();

        while (t < config.getMaxTime() || room.getHumans().size() == 0) {
            room.update(deltaT);

            if (iter % config.getSteps() == 0) {
                room.savePersons(iter);
            }

            t += deltaT;
        }

    }

    private static Config setConfig() {
        return new Config()
                .withN(N);
    }
}
