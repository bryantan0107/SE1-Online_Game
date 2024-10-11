package map;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameController;
import server.exceptions.GameNotReadyException;
import server.exceptions.TooManyHalfMapsException;

public class MapController {
	private final static Logger logger = LoggerFactory.getLogger(MapController.class);
	
	private static List<ServerFullMap> activeFullMaps = new ArrayList<ServerFullMap>();
	private final GameController gameController;
	
	public MapController() {
		gameController = new GameController();
	};
	
	/**
     * Adds a half map to the active full maps list and associates it with the specified game.
     *
     * @param serverHalfMap The half map to add, represented as a 2D array of MapNodes.
     * @param playerId      The ID of the player who submitted the half map.
     * @param gameId        The ID of the game the half map belongs to.
     */
    public void addHalfMap(MapNode[][] serverHalfMap, String playerId, String gameId) {
        HalfMap halfMap = new HalfMap(serverHalfMap, playerId, gameId);

        ServerFullMap fullMap = findFullMap(gameId);
        if (fullMap != null) {
            handleExistingFullMap(fullMap, halfMap, playerId, gameId);
        } else {
            handleNewFullMap(halfMap, gameId);
        }
    }

    
    /* 
	 * From here are the private methods for the MapContoller class.
	 */
	
    
    /**
     * Finds the full map for the specified game ID.
     *
     * @param gameId The game ID to search for.
     * @return The ServerFullMap if found, otherwise null.
     */
    private ServerFullMap findFullMap(String gameId) {
    	for(ServerFullMap fullMap : activeFullMaps) {
			if(fullMap.getGameId().equals(gameId)) {
				return fullMap;
			}
    	}
    	return null;
    }

    /**
     * Handles the case where a full map already exists for the game. This means handling the half map from the second player.
     *
     * @param fullMap   The existing full map.
     * @param halfMap   The new half map to add.
     * @param playerId  The ID of the player who submitted the half map.
     * @param gameId    The ID of the game.
     * @throws TooManyHalfMapsException If the game already has the maximum number of half maps or the same player has already submitted a half map.
     */
    private void handleExistingFullMap(ServerFullMap fullMap, HalfMap halfMap, String playerId, String gameId) {
        if (fullMap.isComplete()) {
            throw new TooManyHalfMapsException("One game should have only maximal 2 halfMaps.");
        } else if (fullMap.getHalfMaps().get(0).getPlayerId().equals(playerId)) {
            throw new TooManyHalfMapsException("Player with id: " + playerId + " has already sent one halfMap.");
        } else {
            gameController.addHalfMap(halfMap, gameId);
            halfMap.setPlayerTwo(true);
        }
    }

    /**
     * Handles the case where no full map exists yet for the game. This means handling the half map from the first player.
     *
     * @param halfMap The new half map to add.
     * @param gameId  The ID of the game.
     * @throws GameNotReadyException If the game is not ready (i.e., not all players have registered).
     */
    private void handleNewFullMap(HalfMap halfMap, String gameId) {
        if (!gameController.checkEnoughPlayers(gameId)) {
            throw new GameNotReadyException("The game is not ready, there's at least one player not yet registered.");
        } else {
            ServerFullMap newFullMap = new ServerFullMap(halfMap, gameId);
            activeFullMaps.add(newFullMap);
            gameController.addFullMap(newFullMap, gameId);
        }
    }
}
