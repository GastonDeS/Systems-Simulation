import models.Config;
import models.Room;

public class ZombieEpidemic {

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
                .withN(30)
                .withDeltaT(0.0125)
                .withVdz(3)
                .withTau(0.5)
                .withApHuman(0.1)
                .withBpHuman(500)
                .withApZombie(0.1)
                .withBpZombie(1000)
                .withApWall(0.1)
                .withBpWall(100)
                .withMaxTime(300)
                .withSteps(5);
    }
}
