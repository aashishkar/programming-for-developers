import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question6 {

    private List<Double> scores;

    public Question6() {
        this.scores = new ArrayList<>();
    }

    public void addScore(double score) {
        scores.add(score);
    }

    public double getMedianScore() {
        Collections.sort(scores);
        int n = scores.size();

        if (n % 2 == 0) {
            // Even number of scores, return average of middle two
            int middleIndex1 = n / 2 - 1;
            int middleIndex2 = n / 2;
            return (scores.get(middleIndex1) + scores.get(middleIndex2)) / 2.0;
        } else {
            // Odd number of scores, return middle score
            int middleIndex = n / 2;
            return scores.get(middleIndex);
        }
    }

    public static void main(String[] args) {
        Question6 scoreTracker = new Question6();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);

        double median1 = scoreTracker.getMedianScore();
        System.out.println("Median 1: " + median1);

        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);

        double median2 = scoreTracker.getMedianScore();
        System.out.println("Median 2: " + median2);
    }
}
