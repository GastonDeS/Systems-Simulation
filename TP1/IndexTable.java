import java.util.ArrayList;
import java.util.List;

public class IndexTable {
    private List<List<List<Particle>>> indexTable;
    private Double cellLength;
    private Integer cellAmount;

    public IndexTable(Double cellLength, Double L) {
        Integer M = (int) Math.ceil((double) L / cellLength);
        this.indexTable = (new ArrayList<>());
        this.cellLength = cellLength;
        this.cellAmount = M;
    }

    public void index(List<Particle> particles) {
        for (int m = 0; m < particles.size() ; m++) {
            for (int i = 0; i < this.cellAmount; i++) {
                for (int j = 0; j < this.cellAmount; j++) {
                    Particle particle = particles.get(m);
                    if ( i * cellLength < particle.getY() && (i + 1) * cellLength > particle.getY() ) {
                        if ( j * cellLength < particle.getX() && ( j + 1) * cellLength > particle.getX() ) {
                            indexTable.get(i).get(j).add(particle);
                        }
                    }
                }
            }
        }
    }

}
