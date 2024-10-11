package map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameController;
import rules.EdgeWaterCountRule;
import rules.FortExistsRule;
import rules.HalfMapExistsRule;
import rules.IslandCheckRule;
import rules.MapSizeRule;
import rules.TerrainCountRule;
import server.exceptions.InvalidMapException;

public class HalfMapValidator {
    private final static Logger logger = LoggerFactory.getLogger(HalfMapValidator.class);
    private final GameController gameController;
    
    public HalfMapValidator() {
    	this.gameController = new GameController();
    };

    /**
     * Validates the half map for various rules including terrain type counts, presence of a fort, absence of islands,
     * and other map constraints. If any validation rule fails, the player loses the game.
     *
     * @param map      The half map to validate, represented as a 2D array of MapNodes.
     * @param playerId The ID of the player who submitted the half map.
     * @param gameId   The ID of the game the half map belongs to.
     * @return True if the half map is valid.
     * @throws InvalidMapException If any validation rule fails.
     */
    public boolean isValid(MapNode[][] map,String playerId, String gameId) {

        try {
        	new HalfMapExistsRule().validate(map);
        	new MapSizeRule().validate(map);
        	new FortExistsRule().validate(map);
            new TerrainCountRule().validate(map);
            new EdgeWaterCountRule().validate(map);
            new IslandCheckRule().validate(map);
        } catch (InvalidMapException e) {
        	gameController.setLose(playerId,gameId);
            logger.error(e.getMessage());
            throw e;
        }

        return true;
    }
}
