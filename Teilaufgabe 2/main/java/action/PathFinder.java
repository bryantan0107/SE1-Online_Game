package action;

import java.util.ArrayDeque;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.MapNode;

/**
 * The path finder that find the shortest distance to the next destination
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class PathFinder {	
	private final static Logger logger = LoggerFactory.getLogger(PathFinder.class);
	
	/**
	 * Main method, find the shortest distance to the possible next destination
	 */
    public static int heuristic(int[] location, MapNode[][] fullMap, boolean collectedTreasure, int[] foundTreasure, int[] myLocation, int[] foundEnemyFort) {
        int minDistance = Integer.MAX_VALUE;
        
        // First find the possible next (non-visited) destination
        for (int i = 0; i < fullMap.length; i++) {
            for (int j = 0; j < fullMap[0].length; j++) {
            	
            	// Skip this non-visited mountain node because all adjacent points are explored
            	if (fullMap[i][j].getType() == 1 && allAdjacentExplored(i, j, fullMap)) {
                    continue;
                }
            	
            	if(collectedTreasure==false) { // haven't collected treasure

            		if(foundTreasure != null) { // treasure location is known
            			int distance =  bfs(location, foundTreasure, fullMap, myLocation);
            			return distance;
                    }
            		
            		// non-visited node + node in myMap area + node type is not water
            		if (!fullMap[i][j].isVisited() && fullMap[i][j].getMyMapArea() && fullMap[i][j].getType()!=3) {
            			// Use bfs to find the shortest distance to the possible next destination
            			int distance =  bfs(location, new int[]{j, i}, fullMap, myLocation);
            			// Confirm the shortest distance to the next destination
            			minDistance = Math.min(minDistance, distance);
            		}
            	} else { // have collected treasure
            		
            		if(foundEnemyFort != null) { // enmyFort location is known
            			int distance =  bfs(location, foundEnemyFort, fullMap, myLocation);
            			return distance;
                    }
            		
            		// non-visited node + node in enemyMap area + node type is not water
            		if (!fullMap[i][j].isVisited() && !fullMap[i][j].getMyMapArea() && fullMap[i][j].getType()!=3) {
            			// Use bfs to find the shortest distance to the possible next destination
        				int distance =  bfs(location, new int[]{j, i}, fullMap, myLocation);
        				// Confirm the shortest distance to the next destination
        				minDistance = Math.min(minDistance, distance);
            		}
            	}
            }
        }
        //logger.info("PathFinder final minDistance: " + minDistance);
        return minDistance;
    }
    
    // Check if all adjacent nodes are explored
    protected static boolean allAdjacentExplored(int x, int y, MapNode[][] fullMap) {
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newX < fullMap.length && newY >= 0 && newY < fullMap[0].length && fullMap[newX][newY].getType()==2) {
                if (!fullMap[newX][newY].isVisited()) {
                    return false; // At least one adjacent point is unexplored
                }
            }
        }
        return true; // All adjacent nodes are explored
    }
    
    // Calculate the shortest distance to the target destination
    protected static int bfs(int[] start, int[] target, MapNode[][] fullMap, int[] myLocation) {
        boolean[][] visited = new boolean[fullMap.length][fullMap[0].length];
        int[][] distance = new int[fullMap.length][fullMap[0].length];

        for (int i = 0; i < fullMap.length; i++) {
            for (int j = 0; j < fullMap[0].length; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        
        int initialDistance = determineNumDistance(fullMap[myLocation[1]][myLocation[0]].getType(), fullMap[start[1]][start[0]].getType(), fullMap);

        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(start);
        distance[start[1]][start[0]] = initialDistance;

        while (!queue.isEmpty()) {
            int[] current = queue.poll(); 
            int x = current[0]; 
            int y = current[1]; 
            visited[y][x] = true;

            // Traverse the four adjacent node of the current node
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k]; 
                int ny = y + dy[k];

                // Check if the adjacent node is valid
                if (nx >= 0 && nx < fullMap[0].length && ny >= 0 && ny < fullMap.length && !visited[ny][nx] && fullMap[ny][nx].getType() != 3) {
                	int newCost = distance[y][x] + determineNumDistance(fullMap[y][x].getType(), fullMap[ny][nx].getType(), fullMap);
                    
                    if (newCost < distance[ny][nx]) {
                        distance[ny][nx] = newCost;
                        queue.add(new int[]{nx, ny});
                    }
                }
            } 
        }    
        return distance[target[1]][target[0]];
    }
    
    // Determine the distance number the move need to use
    public static int determineNumDistance(int terrainType, int targetTerrain, MapNode[][] fullMap) {
        int numMoves=0;
        
        if (terrainType == 2) { // from grass
            if (targetTerrain == 2) { // to grass
                numMoves = 2;
            } else if (targetTerrain == 1) { // to mountain
                numMoves = 3;
            }
        } else if (terrainType == 1) { // from mountain
            if (targetTerrain == 2) { // to grass
                numMoves = 3;
            } else if (targetTerrain == 1) { // to mountain
                numMoves = 4;
            }
        }

        return numMoves;
    }



}
