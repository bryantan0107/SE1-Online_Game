package rules;

import game.GameController;
import server.exceptions.GameIdNotFoundException;

/**
 * Ensures that the provided game ID exists within the game controller.
 */
public class GameIdExistsRule implements IBusinessRule<String> {
    private final GameController gameController;

    public GameIdExistsRule(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Validates that the specified game ID exists.
     *
     * @param gameId The game ID to validate.
     * @throws GameIdNotFoundException If the game ID does not exist.
     */
    @Override
    public void validate(String gameId) throws GameIdNotFoundException {
        if (!gameController.checkGameIDUsed(gameId)) {
            throw new GameIdNotFoundException("GameId: " + gameId + " does not exist.");
        }
    }
}
