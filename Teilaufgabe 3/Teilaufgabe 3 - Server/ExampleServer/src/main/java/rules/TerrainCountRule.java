package rules;

import map.MapNode;
import server.exceptions.InvalidMapException;

/**
 * Implements the business rule from SpielIdee: "Jede Kartenhälfte muss mindestens 10% Bergfelder, 48% Wiesenfelder, 
 * 14% Wasserfelder und 2% Burg beinhalten."
 * 
 * Ensures that each half map contains at least the specified percentage of each terrain type.
 */
public class TerrainCountRule implements IBusinessRule<MapNode[][]> {

	/**
     * Validates the terrain counts in the given half map to ensure they meet the required percentages.
     *
     * @param map The half map to validate.
     * @throws InvalidMapException If the map does not meet the terrain count requirements.
     */
    @Override
    public void validate(MapNode[][] map) throws InvalidMapException {
        int waterNum = 0;
        int mounNum = 0;
        int grassNum = 0;

        // Count the number of each terrain type
        for (int xCoordinate = 0; xCoordinate < 10; xCoordinate++) {
            for (int yCoordinate = 0; yCoordinate < 5; yCoordinate++) {
                switch (map[yCoordinate][xCoordinate].getTerrain()) {
                    case MOUNTAIN:
                        mounNum++;
                        break;
                    case GRASS:
                        grassNum++;
                        break;
                    case WATER:
                        waterNum++;
                        break;
                    default:
                        break;
                }
            }
        }

        // Validate the counts against the required minimums
        validateTerrainCounts(waterNum, mounNum, grassNum);
    }

    /**
     * Validates the terrain counts to ensure they meet the minimum requirements.
     *
     * @param waterNum The number of water tiles.
     * @param mounNum  The number of mountain tiles.
     * @param grassNum The number of grass tiles.
     * @throws InvalidMapException If any terrain type does not meet its required minimum.
     */
    private void validateTerrainCounts(int waterNum, int mounNum, int grassNum) {
        if (waterNum < 7) {
            throw new InvalidMapException("The HalfMap does not have enough water. Required: 7, Found: " + waterNum);
        }
        if (mounNum < 5) {
            throw new InvalidMapException("The HalfMap does not have enough mountains. Required: 5, Found: " + mounNum);
        }
        if (grassNum < 24) {
            throw new InvalidMapException("The HalfMap does not have enough grass. Required: 24, Found: " + grassNum);
        }
    }
}