package map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HalfMapGeneratorTest {
    private HalfMapGenerator halfMapGenerator;
    private MapNode[][] halfMap;

    @BeforeEach
    void setUp() {
        halfMapGenerator = new HalfMapGenerator();
        halfMap = halfMapGenerator.generateHalfMap();
    }

    @Test
    void testGenerateHalfMap() {
        // Test if the generated halfMap is not null
        assertNotNull(halfMap);

        // Test if the halfMap has the correct dimensions
        assertEquals(10, halfMap.length);
        assertEquals(5, halfMap[0].length);
    }

    @Test
    void testPlaceGrass() {
        int grassCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (halfMap[i][j].getType() == 2) {
                    grassCount++;
                }
            }
        }
        assertEquals(26, grassCount, "There should be 26 grass fields");
    }

    @Test
    void testPlaceWater() {
        int waterCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (halfMap[i][j].getType() == 3) {
                    waterCount++;
                }
            }
        }
        assertEquals(7, waterCount, "There should be 7 water fields");
    }

    @Test
    void testPlaceFort() {
        int fortCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (halfMap[i][j].getType() == 4) {
                    fortCount++;
                }
            }
        }
        assertEquals(1, fortCount, "There should be exactly 1 fort field");
    }

    @Test
    void testPlaceMountains() {
        int mountainCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (halfMap[i][j].getType() == 1) {
                    mountainCount++;
                }
            }
        }
        int expectedMountains = 10 * 5 - 26 - 7 - 1;
        assertEquals(expectedMountains, mountainCount, "The remaining fields should be mountains");
    }

    @Test
    void testValidHalfMap() {
        assertTrue(HalfMapValidator.isValid(halfMap), "The generated halfMap should be valid");
    }

    @Test
    void testResetMap() {
        halfMapGenerator.resetMap();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(0, halfMap[i][j].getType(), "After reset, all nodes should be of type 0");
            }
        }
    }
}
