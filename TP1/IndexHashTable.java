import java.util.*;
import java.util.stream.Collectors;

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

    public void resetIndex() {
        for (int i = 0; i < cellAmount; i++) {
            for (int j = 0; j < cellAmount; j++) {
                indexTable.put(hashIndex(i,j), new ArrayList<>());
            }
        }
    }

    private String hashIndex(int i, int j) {
        return String.valueOf(i) + j;
    }

    public Optional<Pair<Integer, Integer>> findParticle(Particle particle) {
        int i = (int) Math.floor(particle.getX() / cellLength);
        int j = (int) Math.floor(particle.getY()/ cellLength);
        boolean found = this.indexTable.get(hashIndex(i, j)).stream().anyMatch(particle::equals);
        return Optional.ofNullable(found? new Pair<>(i ,j): null);
    }

    public List<Particle> addNearParticlesWithFastAlgo(List<Particle> particles, double distance) {
        for (int i =0; i < particles.size(); i++) {
            findCloseParticlesShort(particles.get(i), distance);
        }
        return particles;
    }

    public void findCloseParticlesShort(Particle particle, Double distance) {
        Optional<Pair<Integer, Integer>> ij = this.findParticle(particle);
        if (!ij.isPresent()) return;
        int i = ij.get().getValue1();
        int j = ij.get().getValue2();
        // donde estoy parado
        particle.addNearParticles(this.indexTable.get(hashIndex(i, j)));
        // removeSelf
        particle.getNearParticles().remove(particle);
        // arriba
        addCorners(particle,distance,i-1, j);
        // diagonal abajo
        addCorners(particle,distance,i-1, j+1);
        // derecha
        addCorners(particle,distance, i, j+1);
        // diagonal arriba
        addCorners(particle,distance,i+1, j+1);
    }

    private void addCorners(Particle particle, Double distance, int i , int j) {
        if (j >= 0 && i < cellAmount && j < cellAmount && i >= 0) {
            List<Particle> particles = this.indexTable.get(hashIndex(i, j));
            for (int k = 0; k < particles.size(); k++) {
                if (particles.get(k).distanceToBorder(particle) < distance) {
                    mutualAdd(particles.get(k), particle);
                }
            }
        }
    }

    public void mutualAdd(Particle particle1, Particle particle2) {
        particle1.addNearParticle(particle2);
        particle2.addNearParticle(particle1);
    }

    public List<Particle> findCloseParticles(Particle particle, Double distance) {
        Optional<Pair<Integer, Integer>> ij = this.findParticle(particle);
        List<Particle> particles = new ArrayList<>();
        if (!ij.isPresent()) return particles;
        int i = ij.get().getValue1();
        int j = ij.get().getValue2();
        int ii, fi;
        for (ii = i-1; ii <= i +1 ; ii++){
            for (fi = j-1; fi <= j+1 ; fi++) {
                if (ii >= 0 && ii < cellAmount && fi < cellAmount && fi >= 0) {
                    if (i == ii && j == fi) {
                        particles.addAll(this.indexTable.get(hashIndex(ii, fi)));
                        continue;
                    }
                    particles.addAll(this.indexTable.get(hashIndex(ii, fi)).stream()
                            .filter((particle1 -> particle1.distanceToBorder(particle) <= distance))
                            .collect(Collectors.toList()));
                }
            }
        }
        particles.removeIf((value) -> value.equals(particle));
        return particles;
    }

    public List<Particle> findCloseParticlesCircular(Particle particle, Double distance) {
        Optional<Pair<Integer, Integer>> ij = this.findParticle(particle);
        List<Particle> particles = new ArrayList<>();
        if (!ij.isPresent()) return particles;
        int i = ij.get().getValue1();
        int j = ij.get().getValue2();
        int ii, fi;
        for (ii = i-1; ii <= i +1 ; ii++){
            for (fi = j-1; fi <= j+1 ; fi++) {
                if (ii >= 0 && ii < cellAmount && fi < cellAmount && fi >= 0) {
                    if (i == ii && j == fi) {
                        particles.addAll(this.indexTable.get(hashIndex(ii % cellAmount, fi % cellAmount)));
                        continue;
                    }
                    particles.addAll(this.indexTable.get(hashIndex(ii % cellAmount, fi % cellAmount)).stream()
                            .filter((particle1 -> particle1.circularDistance(particle, cellLength * cellAmount) <= distance))
                            .collect(Collectors.toList()));
                }
            }
        }
        particles.removeIf((value) -> value.equals(particle));
        return particles;
    }

//    private List<Particle> getCircularParticles(Particle particle, Double distance, int i, int j) {
//        Double L = this.cellLength * this.cellAmount;
//        List<Particle> neighbors = new ArrayList<>();
//        if (i == 0) {
//            if (j != 0) neighbors.addAll(this.indexTable.get(hashIndex(this.cellAmount - 1, j - 1)));
//            if (j != this.cellAmount - 1) neighbors.addAll(this.indexTable.get(hashIndex(this.cellAmount - 1, j + 1)));
//            neighbors.addAll(this.indexTable.get(hashIndex(this.cellAmount - 1, j)));
//        }
//        if (i == cellAmount - 1) {
//            if (j != 0) neighbors.addAll(this.indexTable.get(hashIndex(0, j - 1)));
//            if (j != this.cellAmount - 1) neighbors.addAll(this.indexTable.get(hashIndex(0, j + 1)));
//            neighbors.addAll(this.indexTable.get(hashIndex(0, j)));
//        }
//        if (j == 0) {
//            if (i != 0) neighbors.addAll(this.indexTable.get(hashIndex(i - 1, this.cellAmount - 1)));
//            if (i != )
//            neighbors.addAll(this.indexTable.get(hashIndex(i, this.cellAmount - 1)));
//        }
//        if (j == cellAmount - 1) {
//            neighbors.addAll(this.indexTable.get(hashIndex(i, 0)));
//        }
//        return neighbors.stream().filter(p -> p.circularDistance(particle, L) <= distance)
//                .collect(Collectors.toList());
//    }

    public HashMap<String, List<Particle>> getIndexTable() {
        return indexTable;
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
            int i = (int) Math.floor(particle.getX() / cellLength);
            int j = (int) Math.floor(particle.getY()/ cellLength);
            if (i * cellLength <= particle.getX() && (i + 1) * cellLength > particle.getX()) {
                if (j * cellLength <= particle.getY() && (j + 1) * cellLength > particle.getY()) {
                    indexTable.get(hashIndex(i,j)).add(particle);
                }
            }
        }
    }

}
