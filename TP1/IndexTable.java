import java.util.ArrayList;
import java.util.List;

public class IndexTable {
    private List<List<List<Particle>>> indexTable;
    private Double cellLength;
    private Integer cellAmount;

    public IndexTable(Double cellLength, Double L) {
        Integer M = (int) Math.ceil((double) L / cellLength);
        this.indexTable = new ArrayList<>(M);
        this.cellLength = cellLength;
        this.cellAmount = M;

        for (int i = 0; i < this.cellAmount; i++) {
            this.indexTable.add(new ArrayList<>(M));
            for (int j = 0; j < this.cellAmount; j++) {
                this.indexTable.get(i).add(new ArrayList<>());
            }
        }
    }

    public List<Particle> findCloseParticles( Particle particle, Double distance) {
        boolean finded = false;
        int i =0,j =0;
        int sum = 0;
        for (i=0; i < this.cellAmount && !finded; i++) {
            for (j=0; j < this.cellAmount && !finded; j++) {
                for ( Particle particleAux: this.indexTable.get(i).get(j)) {
                    if (particleAux.equals(particle)) {
                        finded = true;
                    }
                }
            }
        }
        List<Particle> particles = new ArrayList<>();
        if (!finded ) return particles;
        int ii, fi;

        for (ii = (i-1 <0 )? 0 : i; ii < i +1; ii++){
            for (fi = (j-1 <0)? 0 : i; fi < j+1 ; fi++) {
                for ( Particle particleAux: this.indexTable.get(ii).get(fi)) {
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
                System.out.print(this.indexTable.get(i).get(j).size()+ " ");
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
                            indexTable.get(i).get(j).add(particle);
                        }
                    }
                }
            }
        }
    }

}
