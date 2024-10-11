package rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.MapNode;
import server.exceptions.InvalidMapException;

/**
 * Implements the business rule from SpielIdee: "Nach Start des Clients registrieren sich die KIs für das Spiel am Server 
 * und erstellen/tauschen danach mit dem Server Kartenhälften aus."
 * 
 * Ensures that the half map provided is not null.
 */

public class HalfMapExistsRule implements IBusinessRule<MapNode[][]> {
	private final static Logger logger = LoggerFactory.getLogger(HalfMapExistsRule.class);

	/**
     * Validates that the provided half map is not null.
     *
     * @param map The half map to validate.
     * @throws InvalidMapException If the half map is null.
     */
    @Override
    public void validate(MapNode[][] map) throws InvalidMapException {
        if (map == null) {
            logger.error("The HalfMap is null!");
            throw new InvalidMapException("The HalfMap should not be null.");
        }
    }
}
