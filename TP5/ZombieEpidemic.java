import models.Person;
import models.Room;

import java.util.ArrayList;
import java.util.List;

public class ZombieEpidemic {

    private static final int PERSONS_AMOUNT = 30;

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Room room = new Room(generatePersons(PERSONS_AMOUNT));
            room.savePersons(i);
        }
    }

    private static List<Person> generatePersons(int n) {
        List<Person> persons = new ArrayList<>();
        // add zombie
        persons.add(new Person(String.valueOf(0), 0, 0,0,0, true));

        // add persons
        for (int i = 1; i < n; i++) {
            persons.add(createPerson(i, persons));
        }

        return persons;
    }

    private static Person createPerson(int i, List<Person> persons) {
        double angle = Math.random() * 2 * Math.PI;
        // 11 - 1 (radio de lejania inicial al zombie) - 2 * radius (radio de la persona y el zombie estan considerados)
        double distance = (Math.random() * (11 - 1 - 2 * Person.getRadius())) + 1 + 2 * Person.getRadius();
        Person person = new Person(
                String.valueOf(i),
                0,
                0,
                Math.cos(angle) * distance,
                Math.sin(angle) * distance,
                false);

        long count = persons.stream().filter(p -> p.isContact(person)).count();
        if (count > 0) return createPerson(i, persons);
        return person;
    }
}
