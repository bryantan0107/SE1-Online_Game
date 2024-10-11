package player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import game.GameController;

public class PlayerController {
	private static List<Player> activePlayers = new ArrayList<Player>();
	private final GameController gameController;
	
	public PlayerController() {
		gameController = new GameController();
	};
	
	/**
     * Creates a new player, adds them to the active players list, and associates them with the specified game.
     * Generates a unique player ID.
     *
     * @param gameId     The ID of the game the player is joining.
     * @param playerInfo The information about the player.
     * @return The unique player ID for the new player.
     */
	public String createNewPlayer(String gameId, List<String> playerInfo) {
		String playerId = generatePlayerId();
		
		// Ensure the generated player ID is unique.
		while(checkPlayerIDUsed(playerId)) {
			playerId = generatePlayerId();
		}
		
        Player player = new Player(gameId,playerId,playerInfo);
        activePlayers.add(player);
        gameController.addPlayer(gameId,player);
        
        return playerId;
	}

	/**
     * Checks if the specified player ID is already in use.
     *
     * @param playerId The player ID to check.
     * @return True if the player ID is in use, false otherwise.
     */
	public boolean checkPlayerIDUsed(String playerId) {
		for (Player player : activePlayers) {
			if (player.getPlayerId().equals(playerId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * Checks if it is the specified player's turn.
     *
     * @param playerId The ID of the player to check.
     * @return True if it is the player's turn, false otherwise.
     */
	public boolean isTurn(String playerId) {
		for (Player player : activePlayers) {
			if (player.getPlayerId().equals(playerId)) {
				if(player.isTurn())
					return true;
			}
		}
		return false;
	}
	
	/**
     * Retrieves the states of the players in the specified game, including the requesting player and their opponent.
     * The opponent's player ID is masked for privacy.
     *
     * @param gameId   The ID of the game.
     * @param playerId The ID of the requesting player.
     * @return A list of players, including the requesting player and a masked opponent.
     */
	public List<Player> getPlayerState(String gameId, String playerId) {
		List<Player> playerStates = new ArrayList<Player>();
		
		// Get the state of the requested player.
		for(Player player : activePlayers) {
			if(player.getGameId().equals(gameId)) {
				if(player.getPlayerId().equals(playerId)) {
					playerStates.add(player);
				}
			}
		}
		
		// Get the state of the opponent player, masking their player ID.
		for(Player player : activePlayers) {
			if(player.getGameId().equals(gameId)) {
				if(!player.getPlayerId().equals(playerId)) {
					// Create a masked player for the opponent.
					Player backupPlayer = new Player(player,generatePlayerId());
					playerStates.add(backupPlayer);
				}
			}
		}
		
		return playerStates;
	}

	public List<Player> getActivePlayers() {
		return activePlayers;
	}

	/* 
	 * From here are the private methods for the PlayerContoller class.
	 */
	
	// Generates a random player ID.
	private String generatePlayerId() {
		return UUID.randomUUID().toString();
	}
}
