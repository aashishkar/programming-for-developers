import java.util.*;

public class Question10 {
    public static void main(String[] args) {
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 6}, {2, 4}, {4, 6}, {4, 5}, {5, 7}}; // Graph representation
        int targetDevice = 4; // Device experiencing power outage

        Set<Integer> impactedDevices = new HashSet<>();
        impactedDevices.add(targetDevice);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(targetDevice);

        while (!queue.isEmpty()) {
            int currentDevice = queue.poll();

            // Check only non-visited neighbors with direct connection to current device
            for (int[] edge : edges) {
                if (edge[0] == currentDevice && !impactedDevices.contains(edge[1])) {
                    impactedDevices.add(edge[1]);
                    queue.add(edge[1]);
                }
            }
        }

        System.out.println("Impacted devices: " + impactedDevices); // Output: Impacted devices: [4, 5, 7]
    }
}
