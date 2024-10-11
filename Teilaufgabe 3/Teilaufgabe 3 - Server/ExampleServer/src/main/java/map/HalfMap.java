package map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HalfMap {
	private final static Logger logger = LoggerFactory.getLogger(HalfMap.class);
	
	private final MapNode[][] halfMap;
	private final String playerId;
	private final String gameId;
	private boolean playerTwo;
	
	public HalfMap(MapNode[][] halfMap, String playerId, String gameId) {
		this.halfMap = halfMap;
		this.playerId = playerId;
		this.gameId = gameId;
		this.playerTwo=false;
	}

	// Retrieves the half map.
	public MapNode[][] getHalfMap() {
		return halfMap;
	}

	// Retrieves the player ID.
	public String getPlayerId() {
		return playerId;
	}

	// Retrieves the game ID.
	public String getGameId() {
		return gameId;
	}

	// Checks if this half map belongs to the second player.
	public boolean isPlayerTwo() {
		return playerTwo;
	}

	// Sets whether this half map belongs to the second player.
	public void setPlayerTwo(boolean playerTwo) {
		this.playerTwo = playerTwo;
	}
}
