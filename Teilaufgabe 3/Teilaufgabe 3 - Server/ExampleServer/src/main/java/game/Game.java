package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import map.ServerFullMap;
import player.EServerPlayerGameState;
import player.Player;

public class Game {
	private final Random random = new Random();
	private final String gameId;
	private final List<Player> players = new ArrayList<>();
	private ServerFullMap fullMap;
	private String gameStateId;

	public Game(String gameId) {
		this.gameId = gameId;
	}

	// Retrieves the game ID.
	public String getGameId() {
		return gameId;
	}
	
	// Retrieves the list of players in the game.
	public List<Player> getPlayers() {
		return players;
	}
	
	// Adds a player to the game and starts the game if there are enough players.
	public void addPlayer(Player player) {
		players.add(player);
		startGame();
	}
	
	// Sets the full map for the game and switches the turn of the players.
	public void setFullMap(ServerFullMap fullMap) {
		this.fullMap = fullMap;
		switchTurn();
	}

	// Retrieves the full map of the game.
	public ServerFullMap getFullMap() {
		return fullMap;
	}
	
	// Switches the turns of the players. Players with state "MustAct" will change to "MustWait" and vice versa.
	public void switchTurn() {
		for (Player eachPlayer : players) {
			if (eachPlayer.getPlayerGameState().equals(EServerPlayerGameState.MustAct)) {
				eachPlayer.setPlayerGameState(EServerPlayerGameState.MustWait);
			} else if (eachPlayer.getPlayerGameState().equals(EServerPlayerGameState.MustWait)) {
				eachPlayer.setPlayerGameState(EServerPlayerGameState.MustAct);
			}
		}
	}
	
	// Sets the player's game state to lost and the other player's state to won.
	public void setLose(String playerId) {
		for(Player eachPlayer : players) {
			if(eachPlayer.getPlayerId().equals(playerId)) {
				eachPlayer.setPlayerGameState(EServerPlayerGameState.Lost);
			} else {
				eachPlayer.setPlayerGameState(EServerPlayerGameState.Won);
			}
		}
	}
	
	// Sets the game state ID.
	public void setGameStateId(String gameStateId) {
		this.gameStateId = gameStateId;
	}
	
	// Retrieves the game state ID.
	public String getGameStateId() {
		return gameStateId;
	}
	
	/* 
	 * From here are the private methods for the Game class.
	 */
	
	// Starts the game if there are enough players by selecting a random player to act first.
	private void startGame() {
		if (players.size() == 2) {
            int firstPlayerIndex = random.nextInt(2); 
            players.get(firstPlayerIndex).setPlayerGameState(EServerPlayerGameState.MustAct);
        }
	}
}
