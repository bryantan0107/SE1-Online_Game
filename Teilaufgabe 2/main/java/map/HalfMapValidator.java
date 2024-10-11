package map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HalfMap validator to check if the HalfMap is valid
 * NodeType: 1=MOUNTAIN, 2=GRASS, 3=WATER
 */
public class HalfMapValidator {
	private final static Logger logger = LoggerFactory.getLogger(HalfMapValidator.class);
	
	// Algorithm for water to avoid island, remove diagonal water
    public static int floodFill(int x, int y, MapNode[][] map) {
    	int numToRemove=0;
    	
        if (x < 0 || x >= 10 || y < 0 || y >= 5 || map[x][y].getType() != 3) {
            return 0;
        }

        // Check and remove diagonal water
        // Lower right
        if (x + 1 < 10 && y + 1 < 5 && map[x + 1][y + 1].getType() == 3) {
            map[x + 1][y + 1] = new MapNode(0);
            numToRemove++;
        }
        // Lower left
        if (x - 1 >= 0 && y + 1 < 5 && map[x - 1][y + 1].getType() == 3) {
            map[x - 1][y + 1] = new MapNode(0);
            numToRemove++;
        }
        // Upper right
        if (x + 1 < 10 && y - 1 >= 0 && map[x + 1][y - 1].getType() == 3) {
            map[x + 1][y - 1] = new MapNode(0);
            numToRemove++;
        }
        // Upper left
        if (x - 1 >= 0 && y - 1 >= 0 && map[x - 1][y - 1].getType() == 3) {
            map[x - 1][y - 1] = new MapNode(0);
            numToRemove++;
        }
        return numToRemove;
    }
    
    // Check if the HalfMap is valid
    public static boolean isValid(MapNode[][] map) {
    	
    	if(map == null) {
    		logger.error("The HalfMap is null!");
    		throw new IllegalArgumentException("The HalfMap is null!");
    	}
    	
        // Check if there is at least one fort in the map
        boolean hasFort = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (map[i][j].getType() == 4) {
                	hasFort = true;
                    break;
                }
            }
            if (hasFort) {
                break;
            }
        }

        if (!hasFort) {
            return false;
        }
        
     // Check if there is at least 7 water, 5 mountains, 24 grass in the map
        int waterNum = 0;
        int mounNum = 0;
        int grassNum = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
            	switch (map[i][j].getType()) {
                case 1:
                    mounNum++;
                    break;
                case 2:
                    grassNum++;
                    break;
                case 3:
                    waterNum++;
                    break;
                default:
                    break;
            }
            }
        }

        if (waterNum<7 || mounNum<5 || grassNum<24) {
            return false;
        }

        // Check water, make sure the water on all four sides of the HalfMap is less than 50%
        // Check vertical sides
        int numOfLeftWater = 0;
        int numOfRightWater = 0;
        for (int y = 0; y < 5; y++) {
            if (map[0][y].getType() == 3)
            	numOfLeftWater++;
            if (map[9][y].getType() == 3)
            	numOfRightWater++;
        }
        if(numOfLeftWater>2 || numOfRightWater>2) {
            return false;
        }

        // Check horizontal sides
        int numOfTopWater = 0;
        int numOfBttmWater = 0;
        for (int x = 0; x < 10; x++) {
            if (map[x][0].getType() == 3)
            	numOfTopWater++;
            if (map[x][4].getType() == 3)
            	numOfBttmWater++;
        }
        if(numOfTopWater>4 || numOfBttmWater>4) {
            return false;
        }

        return true;
    }

}
