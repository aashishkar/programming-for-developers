import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Question7 {

    public static int solveMaze(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        int startX = 0, startY = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                    break;
                }
            }
        }

        
        HashMap<Character, Character> keyDoorMap = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Character.isLowerCase(grid[i][j]) && !keyDoorMap.containsKey(grid[i][j])) {
                    keyDoorMap.put(grid[i][j], ' '); 
                } else if (Character.isUpperCase(grid[i][j])) {
                    keyDoorMap.put(keyDoorMap.get(Character.toLowerCase(grid[i][j])), grid[i][j]);
                }
            }
        }

       
        int moves = 0;
        int[][] visited = new int[m][n]; 
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = 1; 
        HashMap<Character, Boolean> collectedKeys = new HashMap<>(); 

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] curPos = queue.poll();
                int x = curPos[0], y = curPos[1];

                
                if (grid[x][y] == 'E' && isAllKeysCollected(collectedKeys, keyDoorMap)) {
                    return moves;
                }

                
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue; 
                        }
                        int nextX = x + dx, nextY = y + dy;
                        if (nextX < 0 || nextX >= m || nextY < 0 || nextY >= n || grid[nextX][nextY] == 'W' || visited[nextX][nextY] == 1) {
                            continue; 
                        }

                       
                        if (Character.isLowerCase(grid[nextX][nextY])) {
                            collectedKeys.put(grid[nextX][nextY], true);
                            keyDoorMap.put(grid[nextX][nextY], keyDoorMap.get(Character.toUpperCase(grid[nextX][nextY]))); 
                        }

                        
                        if (Character.isUpperCase(grid[nextX][nextY]) && collectedKeys.getOrDefault(Character.toLowerCase(grid[nextX][nextY]), false)) {
                            keyDoorMap.remove(Character.toLowerCase(grid[nextX][nextY])); 
                        }

                        queue.add(new int[]{nextX, nextY});
                        visited[nextX][nextY] = 1; 
                    }
                }
            }
            moves++; 
        }

        return -1; 
    }

    private static boolean isAllKeysCollected(HashMap<Character, Boolean> collectedKeys, HashMap<Character, Character> keyDoorMap) {
        
        for (Character door : keyDoorMap.keySet()) {
            if (!collectedKeys.containsKey(Character.toLowerCase(door)) || !collectedKeys.get(Character.toLowerCase(door))) {
                return false;
            }
        }
        return true;
    }
}

   
