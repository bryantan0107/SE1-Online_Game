package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.exceptions.TooManyHalfMapsException;

public class ServerFullMap {
	private final static Logger logger = LoggerFactory.getLogger(ServerFullMap.class);
	Random random = new Random();
	
	private final List<HalfMap> halfMaps = new ArrayList<>();
	private MapNode[][] fullMap;
	private final String gameId;
	private boolean isComplete;
	
	
    public ServerFullMap(HalfMap halfMap, String gameId) {
		this.gameId = gameId;
		this.halfMaps.add(halfMap);
		this.isComplete=false;
		this.fullMap = null;
	}
    
    // Retrieves the list of half maps.
	public List<HalfMap> getHalfMaps() {
		return new ArrayList<>(halfMaps);
	}
	
	// Retrieves the game ID.
	public String getGameId() {
		return gameId;
	}
	
	// Checks if the full map is complete.
	public boolean isComplete() {
		return isComplete;
	}
	
	// Sets the full map completion status.
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	
	/**
     * Adds a half map to the full map. If the full map becomes complete after adding the half map,
     * it combines the half maps into a full map.
     *
     * @param halfMap The half map to add.
     * @throws TooManyHalfMapsException If more than two half maps are added.
     */
	public void addHalfMap(HalfMap halfMap) {
		if (halfMaps.size() >= 2) {
            throw new TooManyHalfMapsException("One game should have only maximal 2 halfMaps.");
        }
		
		halfMaps.add(halfMap);
		isComplete=true;
		MapNode[][] completedFullMap = combineHalfMaps();
		this.fullMap = completedFullMap;
		logger.info("Game with id: " + gameId + " has now a complete fullMap!");
	}
	
	// Retrieves the full map.
	public MapNode[][] getFullMap() {
		return fullMap;
	}

	// Sets the full map.
	public void setFullMap(MapNode[][] fullMap) {
		this.fullMap = fullMap;
	}

	/* 
	 * From here are the private methods for the ServerFullMap class.
	 */
	
	/**
     * Combines the two half maps into a full map. The combination can be either vertical or horizontal,
     * and the order of the half maps is randomized.
     *
     * @return The combined full map.
     */
	private MapNode[][] combineHalfMaps() {
        boolean randomMapType = random.nextBoolean();
        MapNode[][] halfMap1 = halfMaps.get(0).getHalfMap();
        MapNode[][] halfMap2 = halfMaps.get(1).getHalfMap();
        boolean randomHalfMap = random.nextBoolean();

        if (randomMapType) {
            if (randomHalfMap) {
                return combineVertically(halfMap2, halfMap1);
            } else {
                return combineVertically(halfMap1, halfMap2);
            }
        } else {
            if (randomHalfMap) {
                return combineHorizontally(halfMap2, halfMap1);
            } else {
                return combineHorizontally(halfMap1, halfMap2);
            }
        }
    }

	/**
     * Combines two half maps vertically. The resulting full map has a size of 10x10.
     *
     * @param topHalf    The top half of the map.
     * @param bottomHalf The bottom half of the map.
     * @return The combined full map.
     */
    private MapNode[][] combineVertically(MapNode[][] topHalf, MapNode[][] bottomHalf) {
    	final int ROWS = 5;
        final int COLS = 10;
        MapNode[][] combinedMap = new MapNode[ROWS * 2][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(topHalf[i], 0, combinedMap[i], 0, COLS);
        }
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(bottomHalf[i], 0, combinedMap[i + ROWS], 0, COLS);
        }
        return combinedMap;
    }

    /**
     * Combines two half maps horizontally. The resulting full map has a size of 5x20.
     *
     * @param leftHalf  The left half of the map.
     * @param rightHalf The right half of the map.
     * @return The combined full map.
     */
    private MapNode[][] combineHorizontally(MapNode[][] leftHalf, MapNode[][] rightHalf) {
    	final int ROWS = 5;
        final int COLS = 10;
        MapNode[][] combinedMap = new MapNode[ROWS][COLS * 2];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(leftHalf[i], 0, combinedMap[i], 0, COLS);
            System.arraycopy(rightHalf[i], 0, combinedMap[i], COLS, COLS);
        }
        return combinedMap;
    }

}
