import java.util.Arrays;
import java.util.PriorityQueue;

public class Question2 {

    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        Arrays.sort(engines);

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.add(new int[]{splitCost, 0});

        int totalTime = 0;

        for (int time : engines) {
            int[] currentEngineer = pq.poll();
            totalTime = Math.max(totalTime, currentEngineer[0]);

            if (currentEngineer[0] > splitCost) {
                currentEngineer[0] -= splitCost;
                pq.add(currentEngineer);
            } else {
                totalTime += splitCost - currentEngineer[0];
            }

            pq.add(new int[]{time, 0});
        }

        return totalTime;
    }

    public static void main(String[] args) {
        int[] engines = {3, 4, 5, 2};
        int splitCost = 2;
        System.out.println("Minimum time needed to build all engines: " + minTimeToBuildEngines(engines, splitCost));
    }
}
