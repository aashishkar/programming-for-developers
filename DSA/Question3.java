public class Question3 {

    public static int minimumMoves(int[] dresses) {
        int n = dresses.length;

        if (n == 1) {
            return 0;
        }

        int totalDresses = 0;
        for (int dress : dresses) {
            totalDresses += dress;
        }
        int target = totalDresses / n;

        for (int dress : dresses) {
            if (dress > target + n - 1) {
                return -1;
            }
        }

        int[] diffs = new int[n];
        for (int i = 0; i < n; i++) {
            diffs[i] = dresses[i] - target;
        }

        int left = 0, right = 0, moves = 0;
        int sum = diffs[0];
        
        while (right < n) {
            if (sum == 0) {
                moves += Math.min(right - left, n - right);
                left++;
                sum -= diffs[left - 1];
            } else if (sum > 0) {
                right++;
                if (right < n) {
                    sum += diffs[right];
                }
            } else {
                left++;
                sum -= diffs[left - 1];
            }
        }

        return moves;
    }

    public static void main(String[] args) {
        int[] dresses1 = {2, 1, 3, 0, 2};
        int[] dresses2 = {1, 2, 3, 4};
        int[] dresses3 = {4, 1, 5, 9};

        System.out.println("Minimum moves for [2, 1, 3, 0, 2]: " + minimumMoves(dresses1));
        System.out.println("Minimum moves for [1, 2, 3, 4]: " + minimumMoves(dresses2));
        System.out.println("Minimum moves for [4, 1, 5, 9]: " + minimumMoves(dresses3));
    }
}
