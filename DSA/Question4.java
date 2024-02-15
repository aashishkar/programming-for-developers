import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question4 {

    public static int[] whoKnowsSecret(int n, List<Interval> intervals, int firstPerson) {
        boolean[][] connections = new boolean[n][n];

        // Mark connections based on intervals
        for (Interval interval : intervals) {
            for (int i = interval.start; i <= interval.end; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        connections[firstPerson][j] = true;
                    }
                }
            }
        }

        // Use DFS to find individuals who know the secret
        Set<Integer> knownSecret = new HashSet<>();
        knownSecret.add(firstPerson);
        DFS(connections, knownSecret, firstPerson);

        return knownSecret.stream().mapToInt(i -> i).toArray();
    }

    private static void DFS(boolean[][] connections, Set<Integer> knownSecret, int person) {
        for (int i = 0; i < connections[person].length; i++) {
            if (!knownSecret.contains(i) && connections[person][i]) {
                knownSecret.add(i);
                DFS(connections, knownSecret, i);
            }
        }
    }

    public static class Interval {
        int start;
        int end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        List<Interval> intervals = Arrays.asList(new Interval(0, 2), new Interval(1, 3), new Interval(2, 4));
        int firstPerson = 0;

        int[] knownSecret = whoKnowsSecret(n, intervals, firstPerson);
        System.out.println("Individuals who will eventually know the secret: " + Arrays.toString(knownSecret));
    }
}
