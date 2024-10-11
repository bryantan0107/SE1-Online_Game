package rules;

import player.PlayerController;
import server.exceptions.PlayerIdNotFoundException;

/**
 * Ensures that the provided player ID exists within the player controller.
 */
public class PlayerIdExistsRule implements IBusinessRule<String> {
    private final PlayerController playerController;

    public PlayerIdExistsRule(PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Validates that the specified player ID exists.
     *
     * @param playerId The player ID to validate.
     * @throws PlayerIdNotFoundException If the player ID does not exist.
     */
    @Override
    public void validate(String playerId) throws PlayerIdNotFoundException {
        if (!playerController.checkPlayerIDUsed(playerId)) {
            throw new PlayerIdNotFoundException("PlayerId: " + playerId + " does not exist.");
        }
    }
}