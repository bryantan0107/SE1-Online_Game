package rules;

import map.MapNode;
import map.EMapNodeTerrain;
import server.exceptions.InvalidMapException;

/**
 * Implements the business rule from SpielIdee: "Es muss möglich sein von jedem Kartenfeld jedes andere Kartenfeld,
 * sofern es die Spielregeln erlauben, betreten zu können."
 * 
 * Ensures that it is possible to move from any map field to any other map field, given the game rules allow it.
 */
public class IslandCheckRule implements IBusinessRule<MapNode[][]> {

	/**
     * Validates the given map to ensure there are no isolated land areas that cannot be reached.
     *
     * @param map The half map to validate.
     * @throws InvalidMapException If the map contains isolated land areas.
     */
    @Override
    public void validate(MapNode[][] map) throws InvalidMapException {
    	boolean[][] visited = new boolean[5][10];
        boolean foundNonWater = false;

        // Find the first non-water node and start the flood fill from there
        for (int xCoordinate = 0; xCoordinate < 10; xCoordinate++) {
            for (int yCoordinate = 0; yCoordinate < 5; yCoordinate++) {
                if (map[yCoordinate][xCoordinate].getTerrain() != EMapNodeTerrain.WATER && !visited[yCoordinate][xCoordinate]) {
                    floodFillCheck(xCoordinate, yCoordinate, map, visited);
                    foundNonWater = true;
                    break;
                }
            }
            if (foundNonWater) {
                break; // Exit outer loop
            }
        }

        // Check for any remaining non-water nodes that weren't visited
        for (int xCoordinate = 0; xCoordinate < 10; xCoordinate++) {
            for (int yCoordinate = 0; yCoordinate < 5; yCoordinate++) {
                if (map[yCoordinate][xCoordinate].getTerrain() != EMapNodeTerrain.WATER && !visited[yCoordinate][xCoordinate]) {
                    throw new InvalidMapException("The HalfMap contains an island of water at position (" + xCoordinate + ", " + yCoordinate + ").");
                }
            }
        }
    }

    /**
     * Performs a flood fill to mark all connected land nodes starting from the given coordinates.
     *
     * @param xCoordinate The x-coordinate to start the flood fill.
     * @param yCoordinate The y-coordinate to start the flood fill.
     * @param map         The half map.
     * @param visited     A 2D array to track visited nodes.
     */
    private void floodFillCheck(int xCoordinate, int yCoordinate, MapNode[][] map, boolean[][] visited) {
        if (xCoordinate < 0 || xCoordinate >= 10 || yCoordinate < 0 || yCoordinate >= 5 || visited[yCoordinate][xCoordinate] || map[yCoordinate][xCoordinate].getTerrain() == EMapNodeTerrain.WATER) {
            return;
        }

        visited[yCoordinate][xCoordinate] = true;

        floodFillCheck(xCoordinate + 1, yCoordinate, map, visited);
        floodFillCheck(xCoordinate - 1, yCoordinate, map, visited);
        floodFillCheck(xCoordinate, yCoordinate + 1, map, visited);
        floodFillCheck(xCoordinate, yCoordinate - 1, map, visited);
    }
}
