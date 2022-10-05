import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class VenusMission {

    public static void main(String[] args) {
        List<InitialConditions> earthCond = getInitialValues("TP4/nasaData/earth.csv");
        List<InitialConditions> venusCond = getInitialValues("TP4/nasaData/venus.csv");

        System.out.println(earthCond);
        System.out.println(venusCond);
    }

    private static List<InitialConditions> getInitialValues(String filename) {
        File initConditions = new File(filename);
        List<InitialConditions> initialConditions = new ArrayList<>();
        try (FileReader fileReader = new FileReader(initConditions)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                initialConditions.add(new InitialConditions(Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]), Double.parseDouble(values[5])));
                line = bufferedReader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialConditions;
    }

    /**
     * Initial conditions for a planet at a given time step respective to the sun
     * Velocity is in km/s
     * Distance is in km
     */
    private static class InitialConditions {
        final double x;
        final double y;
        final double velocityX;
        final double velocityY;

        public InitialConditions(double x, double y, double velocityX, double velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        @Override
        public String toString() {
            return "InitialConditions{" +
                    "x=" + x +
                    ", y=" + y +
                    ", velx=" + velocityX +
                    ", vely=" + velocityY +
                    '}';
        }
    }
}
