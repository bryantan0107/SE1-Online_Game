package action;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.MapNode;

class PathFinderTest {
    private MapNode[][] fullMap;
    private int[] location;
    private int[] foundTreasure;
    private int[] myLocation;
    private int[] foundEnemyFort;

    @BeforeEach
    void setUp() {
        fullMap = new MapNode[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                fullMap[i][j] = new MapNode(2); // Default all nodes to grass (type 2)
            }
        }
        
        // Set up some specific terrain and visited nodes
        fullMap[3][3] = new MapNode(1); // Mountain
        fullMap[3][4] = new MapNode(1); // Mountain
        fullMap[4][4] = new MapNode(1); // Mountain
        fullMap[1][1].setVisited(true);
        fullMap[1][2].setVisited(true);
        
        location = new int[]{1, 0};
        foundTreasure = new int[]{5, 5};
        myLocation = new int[]{0, 0};
        foundEnemyFort = new int[]{8, 8};
    }

    @Test
    void testHeuristicNoTreasureCollected() {
        boolean collectedTreasure = false;
        int distance = PathFinder.heuristic(location, fullMap, collectedTreasure, foundTreasure, myLocation, foundEnemyFort);
        assertEquals(20, distance, "Distance to known treasure should be 20 moves");
    }

    @Test
    void testHeuristicTreasureCollected() {
        boolean collectedTreasure = true;
        myLocation = new int[]{5, 5};
        location = new int[]{5, 6};
        int distance = PathFinder.heuristic(location, fullMap, collectedTreasure, foundTreasure, myLocation, foundEnemyFort);
        assertEquals(12, distance, "Distance to known enemy fort should be 12 moves");
    }

    @Test
    void testAllAdjacentExplored() {
        // Set up a node with all adjacent nodes visited
        fullMap[4][5].setVisited(true);
        fullMap[5][4].setVisited(true);
        fullMap[4][3].setVisited(true);
        fullMap[3][4].setVisited(true);
        fullMap[5][5].setVisited(true);
        fullMap[5][5].setVisited(true);
        fullMap[5][3].setVisited(true);
        fullMap[3][3].setVisited(true);
        fullMap[3][5].setVisited(true);
        

        assertTrue(PathFinder.allAdjacentExplored(4, 4, fullMap), "All adjacent nodes should be visited");
    }

    @Test
    void testNotAllAdjacentExplored() {
        // Set up a node with some adjacent nodes not visited
        fullMap[4][4].setVisited(true);
        fullMap[4][3].setVisited(true);

        assertFalse(PathFinder.allAdjacentExplored(4, 4, fullMap), "Not all adjacent nodes should be visited");
    }

    @Test
    void testBFS() {
        int[] start = {1, 0};
        int[] target = {2, 2};
        int distance = PathFinder.bfs(start, target, fullMap, myLocation);
        assertEquals(8, distance, "BFS distance should be 8 moves");
    }

    @Test
    void testDetermineNumDistance() {
        int distance = PathFinder.determineNumDistance(2, 1, fullMap);
        assertEquals(3, distance, "Distance from grass to mountain should be 3 moves");
    }

    @Test
    void testDetermineNumDistanceSameTerrain() {
        int distance = PathFinder.determineNumDistance(2, 2, fullMap);
        assertEquals(2, distance, "Distance from grass to grass should be 2 moves");
    }
}
