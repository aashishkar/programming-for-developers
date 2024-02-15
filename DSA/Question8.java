import java.util.ArrayList;
import java.util.List;

public class Question8 {

    public static List<Integer> findClosestValues(TreeNode root, double target, int x) {
        List<Integer> closestValues = new ArrayList<>();
        TreeNode current = root;
        double minDiff = Double.MAX_VALUE;

       
        while (current != null) {
            double diff = Math.abs(current.val - target);

           
            if (diff < minDiff) {
                closestValues.clear();
                closestValues.add(current.val);
                minDiff = diff;
            } else if (diff == minDiff) {
                
                closestValues.add(current.val);
            }

           
            if (target < current.val) {
                current = current.left;
            } else {
               
                current = current.right;
            }
        }


        if (closestValues.size() < x) {
            return closestValues;
        }

        
        return closestValues.subList(0, x);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double target = 3.8;
        int x = 2;

        List<Integer> closestValues = findClosestValues(root, target, x);

        System.out.println("Closest values: " + closestValues);
    }
}
