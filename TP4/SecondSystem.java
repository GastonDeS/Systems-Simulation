import utils.Planet;
import utils.VenusMission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SecondSystem {
    public static void main(String[] args) {
        List<Planet> earthCond = getInitialValues("TP4/nasaData/earth.csv");
        List<Planet> venusCond = getInitialValues("TP4/nasaData/venus.csv");

        VenusMission venusMission = new VenusMission(venusCond, earthCond);
    }

    private static List<Planet> getInitialValues(String filename) {
        File initConditions = new File(filename);
        List<Planet> initialConditions = new ArrayList<>();
        try (FileReader fileReader = new FileReader(initConditions)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                initialConditions.add(new Planet(Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]), Double.parseDouble(values[5])));
                line = bufferedReader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialConditions;
    }
}
