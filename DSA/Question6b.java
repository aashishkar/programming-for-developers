import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Question6b {

    private PriorityQueue<Double> minHeap;

    public Question6b() {
        minHeap = new PriorityQueue<>(100, Comparator.comparing(Double::doubleValue));
    }

    public void addScore(double score) {
        
        minHeap.add(score);
    }

    public double getMedianScore() {
        int n = minHeap.size();

        if (n % 2 == 0) {
            return (minHeap.poll() + minHeap.poll()) / 2;
        } else {
            
            return minHeap.poll();
        }
    }

    public List<Double> getAllScores() {
        
        List<Double> allScores = new ArrayList<>();

     
        allScores.addAll(minHeap);

        return allScores;
    }

    public boolean existsScore(double score) {
        
        return minHeap.contains(score);
    }

    public static void main(String[] args) {
        
        Question6b tracker = new Question6b();
        tracker.addScore(85.5);
        tracker.addScore(92.3);
        tracker.addScore(77.8);
        tracker.addScore(90.1);

        double median1 = tracker.getMedianScore();
        System.out.println("Median 1: " + median1);

        tracker.addScore(81.2);
        tracker.addScore(88.7);

        double median2 = tracker.getMedianScore();
        System.out.println("Median 2: " + median2);

    }
}
