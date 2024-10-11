package map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HalfMapValidatorTest {

    @Test
    void floodFillTest() {
        MapNode[][] map = new MapNode[10][5];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if ((i + j) % 2 == 0) {
                    map[i][j] = new MapNode(3); // Water
                } else {
                    map[i][j] = new MapNode(2); // Grass
                }
            }
        }

        assertEquals(0, HalfMapValidator.floodFill(5, 2, map));
        assertEquals(0, HalfMapValidator.floodFill(-1, 0, map));
        assertEquals(0, HalfMapValidator.floodFill(10, 0, map));
        assertEquals(0, HalfMapValidator.floodFill(5, -1, map));
        assertEquals(0, HalfMapValidator.floodFill(5, 5, map));

        assertEquals(4, HalfMapValidator.floodFill(1, 1, map)); // Should remove 4 diagonal waters
        assertEquals(0, HalfMapValidator.floodFill(0, 0, map)); // There is no diagonal water anymore
    }
}
