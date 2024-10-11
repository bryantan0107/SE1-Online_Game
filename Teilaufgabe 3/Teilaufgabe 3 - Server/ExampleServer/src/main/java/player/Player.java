package player;

import java.util.List;

public class Player {
	private final String gameId;
	private final String playerId;
	private final String firstName;
	private final String lastName;
	private final String UAccount;
	private EServerPlayerGameState playerGameState;
	private boolean collectedTreasure;
	
	public Player(String gameId, String playerId, List<String> playerInfo) {
		this.gameId = gameId;
		this.playerId = playerId;
		this.firstName = playerInfo.get(0);
		this.lastName = playerInfo.get(1);
		this.UAccount = playerInfo.get(2);
		this.playerGameState = EServerPlayerGameState.MustWait;
		this.collectedTreasure = false;
	}

	/**
     * Constructs a backup Player instance with a random player ID to hide the original player ID.
     *
     * @param backupPlayer   The original player instance to create a backup from.
     * @param randomPlayerId The random player ID to assign to the backup player.
     */
	public Player(Player backupPlayer, String randomPLayerId) {
		this.gameId = backupPlayer.gameId;
		this.playerId = randomPLayerId;
		this.firstName = backupPlayer.firstName;
		this.lastName = backupPlayer.lastName;
		this.UAccount = backupPlayer.UAccount;
		this.playerGameState = backupPlayer.playerGameState;
		this.collectedTreasure = backupPlayer.collectedTreasure;
	}
	
	// Checks if it is the player's turn by verifying if the player game state is 'MustAct'.
	public boolean isTurn() {
		if(playerGameState == EServerPlayerGameState.MustAct) {
			return true;
		}
		return false;
	}

	// Retrieves the player's first name.
	public String getFirstName() {
		return firstName;
	}
	
	// Retrieves the player's last name.
	public String getLastName() {
		return lastName;
	}

	// Retrieves the player's UAccount.
	public String getUAccount() {
		return UAccount;
	}

	// Retrieves the player's current game state.
	public EServerPlayerGameState getPlayerGameState() {
		return playerGameState;
	}

	// Sets the player's game state.
	public void setPlayerGameState(EServerPlayerGameState playerGameState) {
		this.playerGameState = playerGameState;
	}

	// Checks if the player has collected a treasure.
	public boolean isCollectedTreasure() {
		return collectedTreasure;
	}

	// Sets the player's collected treasure status.
	public void setCollectedTreasure(boolean collectedTreasure) {
		this.collectedTreasure = collectedTreasure;
	}

	// Retrieves the game ID associated with the player.
	public String getGameId() {
		return gameId;
	}

	// Retrieves the player's ID.
	public String getPlayerId() {
		return playerId;
	}

}
