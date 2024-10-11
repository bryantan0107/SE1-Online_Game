package rules;

import map.EMapNodeAttribute;
import map.MapNode;
import server.exceptions.InvalidMapException;

/**
 * Implements the business rule from SpielIdee: "Jede Kartenhälfte muss mindestens 10% Bergfelder, 48% Wiesenfelder,
 * 14% Wasserfelder und 2% Burg beinhalten."
 * 
 * Ensures that each half map contains at least one fort.
 */
public class FortExistsRule implements IBusinessRule<MapNode[][]> {

	/**
     * Validates the given map to ensure it contains at least one fort.
     *
     * @param map The half map to validate.
     * @throws InvalidMapException If the map does not contain a fort.
     */
    @Override
    public void validate(MapNode[][] map) throws InvalidMapException {
        for (int xCoordinate = 0; xCoordinate < 10; xCoordinate++) {
            for (int yCoordinate = 0; yCoordinate < 5; yCoordinate++) {
                if (map[yCoordinate][xCoordinate].hasAttribute(EMapNodeAttribute.MY_FORT) ||
                    map[yCoordinate][xCoordinate].hasAttribute(EMapNodeAttribute.ENEMY_FORT)) {
                    return;
                }
            }
        }
        throw new InvalidMapException("The HalfMap does not contain a fort.");
    }
}