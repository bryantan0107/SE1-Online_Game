package rules;

import map.MapNode;
import server.exceptions.InvalidMapException;

/**
 * Implements the business rule from SpielIdee: "Hierzu erstellt jede der beiden KIs zufällig eine Hälfte der finalen Spielkarte 
 * (mit je 5 x 10 Feldern)."
 * 
 * Ensures that each half map is of the correct size, specifically 5 rows by 10 columns.
 */

public class MapSizeRule implements IBusinessRule<MapNode[][]>  {
    private static final int EXPECTED_ROWS = 5;
    private static final int EXPECTED_COLS = 10;

    /**
     * Validates the size of the given half map.
     *
     * @param map The half map to validate.
     * @throws InvalidMapException If the map size is incorrect.
     */
    @Override
    public void validate(MapNode[][] map) {
        if (map.length != EXPECTED_ROWS || map[0].length != EXPECTED_COLS) {
            throw new InvalidMapException("The HalfMap size is incorrect. Expected size is 5x10.");
        }
    }
}
