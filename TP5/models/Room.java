package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Room {
    List<Person> persons;

    public Room(List<Person> persons) {
        this.persons = persons;
    }




    public void savePersons(int iteration) {
        try {
            File positions = new File(String.format("TP5/position/positions%d.xyz", iteration));
            FileWriter positionsFile = new FileWriter(positions);

            positionsFile.write((1+persons.size()) + "\n" +
                    "Lattice=\"1 0.0 0.0 0.0 1 0.0 0.0 0.0 1\"" +
                    "\n");
            positionsFile.write("-1 0 0 11 Background\n");
            for (Person p : persons) {
                positionsFile.write(p + "\n");
            }

            positionsFile.close();
        } catch (IOException e) {
            System.out.println("Add folder TP5/position");
        }
    }
}
