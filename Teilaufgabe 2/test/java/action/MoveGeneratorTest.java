package action;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.MapNode;

public class MoveGeneratorTest {
	private MoveGenerator moveGenerator;
    private MapNode[][] fullMap;
    private int[] myLocation;

    @BeforeEach
    void setUp() {
        moveGenerator = new MoveGenerator();
        fullMap = new MapNode[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                fullMap[i][j] = new MapNode(2);
                fullMap[i][j].setMyMapArea(true);
            }
        }
        myLocation = new int[] {2, 2};
        moveGenerator.updateVisitedArea(myLocation, fullMap);
    }

    @Test
    void testUpdateVisitedArea() {
    	moveGenerator.updateVisitedArea(myLocation, fullMap);
        assertTrue(fullMap[2][2].isVisited());
        // Check if the adjacent nodes to a mountain are marked as visited
        fullMap[2][2].setType(1); // Set current position as mountain
        moveGenerator.updateVisitedArea(myLocation, fullMap);
        assertTrue(fullMap[1][1].isVisited());
        assertTrue(fullMap[1][2].isVisited());
        assertTrue(fullMap[1][3].isVisited());
        assertTrue(fullMap[2][1].isVisited());
        assertTrue(fullMap[2][3].isVisited());
        assertTrue(fullMap[3][1].isVisited());
        assertTrue(fullMap[3][2].isVisited());
        assertTrue(fullMap[3][3].isVisited());
    }

    @Test
    void testGenerateMove() {
        int[] move = moveGenerator.generateMove();
        assertNotNull(move);
        assertTrue(move[0] == -1 || move[0] == 1 || move[0] == 0);
        assertTrue(move[1] == -1 || move[1] == 1 || move[1] == 0);
    }

    @Test
    void testIsValidMove() {
        assertTrue(moveGenerator.isValidMove(1, 1, 5, 5, fullMap));
        assertFalse(moveGenerator.isValidMove(5, 5, 5, 5, fullMap));
        assertFalse(moveGenerator.isValidMove(-1, -1, 5, 5, fullMap));
        
        fullMap[1][1].setType(3); // set a node as water
        assertFalse(moveGenerator.isValidMove(1, 1, 5, 5, fullMap));
    }

    @Test
    void testGetPossibleMoves() {
        var possibleMoves = moveGenerator.getPossibleMoves();
        assertEquals(4, possibleMoves.size());
        assertTrue(possibleMoves.stream().anyMatch(move -> Arrays.equals(move, new int[]{-1, 0})));
        assertTrue(possibleMoves.stream().anyMatch(move -> Arrays.equals(move, new int[]{1, 0})));
        assertTrue(possibleMoves.stream().anyMatch(move -> Arrays.equals(move, new int[]{0, 1})));
        assertTrue(possibleMoves.stream().anyMatch(move -> Arrays.equals(move, new int[]{0, -1})));
    }

    @Test
    void testDetermineNumMovesToSend() {
        int[] move = {1, 0}; // move to the right
        fullMap[2][2].setType(2); // grass
        fullMap[2][3].setType(1); // mountain
        assertEquals(3, moveGenerator.determineNumMovesToSend(move));

        fullMap[2][2].setType(1); // mountain
        fullMap[2][3].setType(2); // grass
        assertEquals(3, moveGenerator.determineNumMovesToSend(move));

        fullMap[2][2].setType(2); // grass
        fullMap[2][3].setType(2); // grass
        assertEquals(2, moveGenerator.determineNumMovesToSend(move));

        fullMap[2][2].setType(1); // mountain
        fullMap[2][3].setType(1); // mountain
        assertEquals(4, moveGenerator.determineNumMovesToSend(move));
    }
}
