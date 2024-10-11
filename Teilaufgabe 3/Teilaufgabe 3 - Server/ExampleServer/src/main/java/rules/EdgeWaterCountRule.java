package rules;

import map.MapNode;
import map.EMapNodeTerrain;
import server.exceptions.InvalidMapException;

/**
 * Implements the business rule from SpielIdee: "Um den Wechsel zwischen beiden Kartenhälften sicherzustellen,
 * egal wie diese später vom Server kombiniert werden, müssen ≥ 51% der Felder jedes Randes betretbar sein."
 * 
 * Ensures that at least 51% of the fields on each edge of the half map are accessible.
 */
public class EdgeWaterCountRule implements IBusinessRule<MapNode[][]> {

	/**
     * Validates the given map to enruse that the number of water nodes on the edges of the map compliance with the rule.
     *
     * @param map The half map to validate.
     * @throws InvalidMapException If the map does not comply with the edge water count rule.
     */
    @Override
    public void validate(MapNode[][] map) throws InvalidMapException {
    	int numOfLeftWater = 0;
        int numOfRightWater = 0;
        for (int y = 0; y < 5; y++) {
            if (map[y][0].getTerrain() == EMapNodeTerrain.WATER)
                numOfLeftWater++;
            if (map[y][9].getTerrain() == EMapNodeTerrain.WATER)
                numOfRightWater++;
        }
        if (numOfLeftWater > 2 || numOfRightWater > 2) {
            throw new InvalidMapException("At least 51% of the fields on each edge must be accessible.");
        }

        int numOfTopWater = 0;
        int numOfBttmWater = 0;
        for (int x = 0; x < 10; x++) {
            if (map[0][x].getTerrain() == EMapNodeTerrain.WATER)
                numOfTopWater++;
            if (map[4][x].getTerrain() == EMapNodeTerrain.WATER)
                numOfBttmWater++;
        }
        if (numOfTopWater > 4 || numOfBttmWater > 4) {
            throw new InvalidMapException("At least 51% of the fields on each edge must be accessible.");
        }
    }
}
