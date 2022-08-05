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
