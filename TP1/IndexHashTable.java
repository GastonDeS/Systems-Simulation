import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexHashTable {
    private HashMap< String, List<Particle>> indexTable;
    private Double cellLength;
    private Integer cellAmount;

    public IndexHashTable(Double cellLength, Double L) {
        Integer M = (int) Math.ceil((double) L / cellLength);
        this.indexTable = new HashMap<>();
        this.cellLength = cellLength;
        this.cellAmount = M;

        for (int i = 0; i < this.cellAmount; i++) {
            for (int j = 0; j < this.cellAmount; j++) {
                this.indexTable.put(hashIndex(i, j), new ArrayList<>());
            }
        }
    }

    private String hashIndex(int i, int j) {
        return String.format("%d%d",i,j);
    }

    public List<Particle> findCloseParticles( Particle particle, Double distance) {
        boolean finded = false;
        int i =0,j =0;
        int sum = 0;
        for (i=0; i < this.cellAmount && !finded; i++) {
            for (j=0; j < this.cellAmount && !finded; j++) {
                for ( Particle particleAux: this.indexTable.get(hashIndex(i, j))) {
                    if (particleAux.equals(particle)) {
                        finded = true;
                    }
                }
            }
        }
        List<Particle> particles = new ArrayList<>();
        if (!finded ) return particles;
        int ii, fi;

        for (ii = (i-1 <0 )? 0 : i; ii < i +1 && ii < cellAmount; ii++){
            for (fi = (j-1 <0)? 0 : i; fi < j+1 && fi < cellAmount ; fi++) {
                for ( Particle particleAux: this.indexTable.get(hashIndex(ii,fi))) {
                    if ( particleAux.distance(particle) <= distance ) {
                        particles.add(particleAux);
                    }
                }
            }
        }
        particles.removeIf((value) -> value.equals(particle));
        return particles;
    }

    public void printTable() {
        for (int i = 0; i < this.cellAmount; i++) {
            for (int j = 0; j < this.cellAmount; j++) {
                System.out.print(this.indexTable.get(hashIndex(i, j)).size()+ " ");
            }
            System.out.println();
        }
    }

    public void index(List<Particle> particles) {
        for (Particle particle : particles) {
            for (int i = 0; i < this.cellAmount; i++) {
                for (int j = 0; j < this.cellAmount; j++) {
                    if (i * cellLength <= particle.getX() && (i + 1) * cellLength > particle.getX()) {
                        if (j * cellLength <= particle.getY() && (j + 1) * cellLength > particle.getY()) {
                            indexTable.get(hashIndex(i,j)).add(particle);
                        }
                    }
                }
            }
        }
    }

}
