import java.util.PriorityQueue;

public class Question5 {

    private PriorityQueue<Double> minHeap; // Stores lower half of scores
    private PriorityQueue<Double> maxHeap; // Stores higher half of scores

    public Question5() {
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b, a)); // Reverse max-heap for higher values
    }

    public void addScore(double score) {
        if (maxHeap.isEmpty() || score <= maxHeap.peek()) {
            maxHeap.add(score);
        } else {
            minHeap.add(score);
        }
        balanceHeaps();
    }

    public double getMedianScore() {
        if (minHeap.size() == maxHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            return minHeap.peek();
        }
    }

    private void balanceHeaps() {
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.add(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size() + 1) {
            maxHeap.add(minHeap.poll());
        }
    }
}
