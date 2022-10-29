import models.Config;
import models.Room;

public class ZombieEpidemic {

    private static final int N = 30;

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            final Config config = setConfig();
            Room room = new Room(config);
            room.fillRoom();
            room.savePersons(i);
        }
    }

    private static Config setConfig() {
        return new Config()
                .withN(N);
    }
}
