import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        List<Particle> particles = new ArrayList<>();

        particles.add(new Particle(0.0, 0.0));
        particles.add(new Particle(0.1, .5));
        particles.add(new Particle(.3, .3));
        particles.add(new Particle(.8, .2));
        particles.add(new Particle(.7, .2));

        IndexTable index = new IndexTable(0.1, 1.0);
        index.index(particles);

        index.printTable();
    }
}
