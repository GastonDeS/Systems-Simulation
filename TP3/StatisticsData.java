public class StatisticsData {
    public double eventsSize;
    public double totalTime;

    public StatisticsData(int eventsSize, double totalTime) {
        this.eventsSize = eventsSize;
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return ""+ totalTime;
    }
}
