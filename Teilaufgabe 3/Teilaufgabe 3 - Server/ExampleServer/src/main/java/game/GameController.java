package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.HalfMap;
import map.ServerFullMap;
import player.Player;

public class GameController {
	private final static Logger logger = LoggerFactory.getLogger(GameController.class);
	
	private final int MAX_GAMES = 99;
	private static List<Game> activeGames = new ArrayList<Game>();
	
	public GameController() {}
	
	/**
     * Creates a new game and adds it to the active games list.
     * Generates a unique game ID, ensuring it is not already in use.
     * If the maximum number of active games is reached, removes the oldest game.
     *
     * @return The unique game ID for the new game.
     */
	public String createNewGame() {
		// Remove the oldest game if the number of active games exceeds the maximum limit.
		if(activeGames.size() >= MAX_GAMES) {
			activeGames.remove(0);
		}
		
		String gameId = generateGameId();
		
		// Ensure the generated game ID is unique.
		while(checkGameIDUsed(gameId)) {
			gameId = generateGameId();
		}
		
        Game game = new Game(gameId);
        activeGames.add(game);
        
        return gameId;
    }
	
	/**
     * Checks if the given game ID is already in use.
     *
     * @param gameId The game ID to check.
     * @return True if the game ID is in use, false otherwise.
     */
	public boolean checkGameIDUsed(String gameId) {
		for (Game eachGame : activeGames) {
			if (eachGame.getGameId().equals(gameId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * Checks if the game with the given ID has enough players (at least 2 players).
     *
     * @param gameId The game ID to check.
     * @return True if the game has at least 2 players, false otherwise.
     */
	public boolean checkEnoughPlayers(String gameId) {
		for (Game eachGame : activeGames) {
			if (eachGame.getGameId().equals(gameId)) {
				if(eachGame.getPlayers().size()>=2)
					return true;
			}
		}
		return false;
	}
	
	/**
     * Adds a player to the game with the given ID.
     *
     * @param gameId The game ID.
     * @param player The player to add.
     */
	public void addPlayer(String gameId, Player player) {
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId))
				eachGame.addPlayer(player);
		}
	}
	
	/**
     * Adds a full map to the game with the given ID.
     *
     * @param fullMap The full map to add.
     * @param gameId The game ID.
     */
	public void addFullMap(ServerFullMap fullMap, String gameId) {
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId))
				eachGame.setFullMap(fullMap);
		}
	}
	
	/**
     * Checks if the half-map being submitted is from the second player by verifying if the full map is already created.
     *
     * @param gameId The game ID.
     * @return True if it is the second player's half-map, false otherwise.
     */
	public boolean isPlayerTwoHalfMap(String gameId) {
		boolean isPlayerTwo=false;
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId)) {
				if(eachGame.getFullMap()!=null) {
					isPlayerTwo=true;
				}
			}
		}
		return isPlayerTwo;
	}
	
	/**
     * Adds a half-map of the second player to the full map of the game with the given ID and switches the turn.
     *
     * @param halfMap The half-map to add.
     * @param gameId The game ID.
     */
	public void addHalfMap(HalfMap halfMap, String gameId) {
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId)) {
				eachGame.getFullMap().addHalfMap(halfMap);
				eachGame.switchTurn();
			}
		}
	}
	
	/**
     * Retrieves the full map of the game with the given ID.
     *
     * @param gameId The game ID.
     * @return The full map if available, null otherwise.
     */
	public ServerFullMap getFullMap(String gameId) { 
		for(Game eachGame : getActiveGames()) {
			if(eachGame.getGameId().equals(gameId)) {
				return eachGame.getFullMap();
			}
		}
		return null;
	}
	
	/**
     * Sets the player's state to lost in the game with the given ID.
     *
     * @param playerId The player ID.
     * @param gameId The game ID.
     */
	public void setLose(String playerId, String gameId) {
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId)) {
				eachGame.setLose(playerId);
			}
		}
	}
	
	/**
     * Generates a unique game state ID for the game with the given ID.
     *
     * @param gameId The game ID.
     * @return The generated game state ID.
     */
	public String generateGameStateId(String gameId) {
		String gameStateId = UUID.randomUUID().toString();
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId)) {
				eachGame.setGameStateId(gameStateId);
			}
		}
		return gameStateId;
	}
	
	/**
     * Retrieves the game state ID of the game with the given ID, generating a new one if necessary.
     *
     * @param gameId The game ID.
     * @return The game state ID.
     */
	public String getGameStateId(String gameId) {
		String gameStateID = null;
		for(Game eachGame : getActiveGames()) {
			if(eachGame.getGameId().equals(gameId)) {
				if(eachGame.getGameStateId()==null) {
					gameStateID = generateGameStateId(gameId);
				} else {
					gameStateID = eachGame.getGameStateId();
				}
			}
		}
		return gameStateID;
	}
	
	/**
     * Determine if the player with the given ID is the second player in the game with the given ID.
     *
     * @param gameId The game ID.
     * @param playerId The player ID.
     * @return True if the player is the second player, false otherwise.
     */
	public boolean isPlayerTwo(String gameId, String playerId) {
		boolean isPlayerTwo=false;
		for(Game eachGame : activeGames) {
			if(eachGame.getGameId().equals(gameId)) {
				if(eachGame.getFullMap()!=null) {
					for(HalfMap halfMap : eachGame.getFullMap().getHalfMaps()) {
						if(halfMap.getPlayerId().equals(playerId)) {
							if(halfMap.isPlayerTwo()) {
								isPlayerTwo=true;
							}
						}
					}
				}
			}
		}
		return isPlayerTwo;
	}
	
	// Retrieves the list of active games.
	public List<Game> getActiveGames() {
		return activeGames;
	}
	
	/* 
	 * From here are the private methods for the GameContoller class.
	 */
	
	/**
     * Generates a random string to be used as a game ID.
     *
     * @return The generated game ID.
     */
	private String generateGameId() {
		final int ID_LENGTH = 5;
		Random random = new Random();
        String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder gameId = new StringBuilder(5);
        
        for (int idChar = 0; idChar < ID_LENGTH; idChar++) {
            int index = random.nextInt(allChars.length());
            gameId.append(allChars.charAt(index));
        }
        
        return gameId.toString();
	}
}
