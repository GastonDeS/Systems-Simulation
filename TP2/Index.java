import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Index {
    private HashMap< String, List<Agent>> indexTable;
    private Double cellLength;
    private Integer cellAmount;

    public Index(Double cellLength, Double L) {
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
        return ""+i + j;
    }

    public Optional<Pair<Integer, Integer>> findAgent(Agent Agent) {
        int i = (int) Math.floor(Agent.getX() / cellLength);
        int j = (int) Math.floor(Agent.getY()/ cellLength);
        boolean found = this.indexTable.get(hashIndex(i, j)).stream().anyMatch(Agent::equals);
        return Optional.ofNullable(found? new Pair<>(i ,j): null);
    }

    public List<Agent> addNearAgentsWithFastAlgo(List<Agent> Agents, double distance, boolean circular) {
        for (int i =0; i < Agents.size(); i++) {
            findCloseAgentsShort(Agents.get(i), distance, circular);
        }
        return Agents;
    }

    public void findCloseAgentsShort(Agent Agent, Double distance, boolean circular) {
        Optional<Pair<Integer, Integer>> ij = this.findAgent(Agent);
        if (!ij.isPresent()) return;
        int i = ij.get().getValue1();
        int j = ij.get().getValue2();
        // donde estoy parado
        Agent.addNearAgents(this.indexTable.get(hashIndex(i, j)));
        // removeSelf
        Agent.getNearAgents().remove(Agent);

        // arriba
        addCorners(Agent, distance, (i - 1) , j, circular);
        // diagonal abajo
        addCorners(Agent, distance, (i - 1) , (j + 1) , circular);
        // derecha
        addCorners(Agent, distance, i, (j + 1) , circular);
        // diagonal arriba
        addCorners(Agent, distance, (i + 1) , (j + 1), circular);
    }

    private void addCorners(Agent Agent, Double distance, int i , int j, boolean circular) {
        if (circular) {
            listMutualAdd( Agent, distance, (i+cellAmount) % cellAmount, (j+cellAmount) % cellAmount);
        } else if ( j >= 0 && i < cellAmount && j < cellAmount && i >= 0) {
            listMutualAdd( Agent, distance, i , j);
        }
    }

    private void listMutualAdd(Agent Agent, Double distance, int i , int j) {
        List<Agent> Agents = this.indexTable.get(hashIndex(i, j));
        if (Agents == null)  {
            System.out.println("i: "+i+ " j: "+j);
            return;
        }
        for (int k = 0; k < Agents.size(); k++) {
            if (Agents.get(k).distanceToBorder(Agent) < distance) {
                mutualAdd(Agents.get(k), Agent);
            }
        }
    }

    public void mutualAdd(Agent Agent1, Agent Agent2) {
        Agent1.addNearAgent(Agent2);
        Agent2.addNearAgent(Agent1);
    }

    public List<Agent> findCloseAgents(Agent Agent, Double distance) {
        Optional<Pair<Integer, Integer>> ij = this.findAgent(Agent);
        List<Agent> Agents = new ArrayList<>();
        if (!ij.isPresent()) return Agents;
        int i = ij.get().getValue1();
        int j = ij.get().getValue2();
        int ii, fi;
        for (ii = i-1; ii <= i +1 ; ii++){
            for (fi = j-1; fi <= j+1 ; fi++) {
                if (ii >= 0 && ii < cellAmount && fi < cellAmount && fi >= 0) {
                    if (i == ii && j == fi) {
                        Agents.addAll(this.indexTable.get(hashIndex(ii, fi)));
                        continue;
                    }
                    Agents.addAll(this.indexTable.get(hashIndex(ii, fi)).stream()
                            .filter((Agent1 -> Agent1.distanceToBorder(Agent) <= distance))
                            .collect(Collectors.toList()));

                }
            }
        }
        Agents.removeIf((value) -> value.equals(Agent));
        return Agents;
    }

    public List<Agent> findCloseAgentsCircular(Agent Agent, Double distance) {
        Optional<Pair<Integer, Integer>> ij = this.findAgent(Agent);
        List<Agent> Agents = new ArrayList<>();
        if (!ij.isPresent()) return Agents;
        int i = ij.get().getValue1();
        int j = ij.get().getValue2();
        int ii, fi;
        for (ii = i-1; ii <= i +1 ; ii++){
            for (fi = j-1; fi <= j+1 ; fi++) {
                if (ii >= 0 && ii < cellAmount && fi < cellAmount && fi >= 0) {
                    if (i == ii && j == fi) {
                        Agents.addAll(this.indexTable.get(hashIndex(ii % cellAmount, fi % cellAmount)));
                        continue;
                    }
                    Agents.addAll(this.indexTable.get(hashIndex(ii % cellAmount, fi % cellAmount)).stream()
                            .filter((Agent1 -> Agent1.circularDistance(Agent, cellLength * cellAmount) <= distance))
                            .collect(Collectors.toList()));
                }
            }
        }
        Agents.removeIf((value) -> value.equals(Agent));
        return Agents;
    }

//    private List<Agent> getCircularAgents(Agent Agent, Double distance, int i, int j) {
//        Double L = this.cellLength * this.cellAmount;
//        List<Agent> neighbors = new ArrayList<>();
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
//        return neighbors.stream().filter(p -> p.circularDistance(Agent, L) <= distance)
//                .collect(Collectors.toList());
//    }

    public HashMap<String, List<Agent>> getIndexTable() {
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

    public void index(List<Agent> Agents) {
        for (Agent Agent : Agents) {
            int i = (int) Math.floor(Agent.getX() / cellLength);
            int j = (int) Math.floor(Agent.getY()/ cellLength);
            if (i * cellLength <= Agent.getX() && (i + 1) * cellLength > Agent.getX()) {
                if (j * cellLength <= Agent.getY() && (j + 1) * cellLength > Agent.getY()) {
                    indexTable.get(hashIndex(i,j)).add(Agent);
                }
            }
        }
    }

}
