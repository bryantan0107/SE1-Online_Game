package rules;

import game.GameController;
import server.exceptions.TooManyPlayersException;

/**
 * Implements the business rule from SpielIdee: "Die grundlegende Spielidee ist, dass zwei Clients bzw. deren KIs auf der gleichen Spielkarte
 * eine vergleichbare Aufgabe erfüllen müssen."
 * 
 * Ensures that no more than two players are registered in a single game.
 */

public class PlayerCountRule implements IBusinessRule<String> {
    private final GameController gameController;

    public PlayerCountRule(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Validates that the specified game does not have more than two players.
     *
     * @param gameId The ID of the game to validate.
     * @throws TooManyPlayersException If the game has more than two players.
     */
    @Override
    public void validate(String gameId) throws TooManyPlayersException {
        if (gameController.checkEnoughPlayers(gameId)) {
            throw new TooManyPlayersException("Game with GameId: " + gameId + " should have only maximal 2 players.");
        }
    }
}