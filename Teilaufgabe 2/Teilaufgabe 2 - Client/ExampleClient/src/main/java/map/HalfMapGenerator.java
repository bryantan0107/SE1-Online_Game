package map;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ui.CLI;

/**
 * The HalfMap Generator generate my HalfMap to send to the Server
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class HalfMapGenerator {
	private final static Logger logger = LoggerFactory.getLogger(HalfMapGenerator.class);
	
    private static MapNode[][] map;
    private static Random random = new Random();

    // Initialite HalfMapGenerator and create a HalfMap's array
    public HalfMapGenerator() {
        map = new MapNode[10][5];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
            	map[i][j] = new MapNode(0);
            }
        }
    }

    // Place all the fields in the HalfMap, reset and place again if it's invalid
    public static MapNode[][] generateHalfMap() {
        logger.info("Generating new HalfMap...");
        map = new MapNode[10][5];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
            	map[i][j] = new MapNode(0);
            }
        }
        
        placeGrass();
        placeWater();
        placeFort();
        placeMountains();

        while (!HalfMapValidator.isValid(map)) {
        	logger.error("The HalfMap is not valid, regenerating new HalfMap...");
            resetMap();
            placeGrass();
            placeWater();
            placeFort();
            placeMountains();
        }
        logger.info("Successfully generated a new HalfMap.");
        CLI.displayHalfMap(map);
        return map;
    }

    // Place 26 fields in random location in HalfMap
    private static void placeGrass() {
        for (int i = 0; i < 26; i++) {
            int x = random.nextInt(10);
            int y = random.nextInt(5);

            while (map[x][y].getType() != 0) {
                x = random.nextInt(10);
                y = random.nextInt(5);
            }
            map[x][y] = new MapNode(2);
        }
    }

    // Place 7 water in random location in HalfMap, use floodFill algorithm to avoid island
    private static void placeWater() {
        int waterCount = 0;

        while (waterCount < 7) {
            int x = random.nextInt(10);
            int y = random.nextInt(5);

            while (map[x][y].getType() != 0) {
                x = random.nextInt(10);
                y = random.nextInt(5);
            }
            
            map[x][y] = new MapNode(3);
            waterCount++;
            
            waterCount -= HalfMapValidator.floodFill(x, y, map);
        }
    }

    // Place my fort in a random location in HalfMap
    private static void placeFort() {
        int x = random.nextInt(10);
        int y = random.nextInt(5);

        while (map[x][y].getType() != 0) {
            x = random.nextInt(10);
            y = random.nextInt(5);
        }

        map[x][y] = new MapNode(4);
    }
    
    // Place mountains in the rest of the location in HalfMap
    private static void placeMountains() {  
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
            	while (map[i][j].getType() == 0) {
            		map[i][j] = new MapNode(1);
                }
            }
        }       
    }

    // Rest all the HalfMap node
    protected static void resetMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                map[i][j] = new MapNode(0);
            }
        }
    }

    public MapNode[][] getHalfMap() {
        return map;
    }
}
