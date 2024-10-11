package rules;

import player.PlayerController;

/**
 *  The business rule come from SpielIdee: "Eine KI darf aber keine Aktion setzen solange die andere KI an der Reihe ist."
 */

import server.exceptions.NotYourTurnException;

/**
 * Implements the business rule from SpielIdee: "Eine KI darf aber keine Aktion setzen solange die andere KI an der Reihe ist."
 * 
 * Ensures that a player cannot take an action if it is not their turn.
 */
public class PlayerTurnRule implements IBusinessRule<String> {
    private final PlayerController playerController;

    public PlayerTurnRule(PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Validates that it is the specified player's turn to act.
     *
     * @param playerId The ID of the player to validate.
     * @throws NotYourTurnException If it is not the player's turn.
     */
    @Override
    public void validate(String playerId) throws NotYourTurnException {
        if (!playerController.isTurn(playerId)) {
            throw new NotYourTurnException("It's not your turn yet (Player Id: " + playerId + "), please wait for another player to act first.");
        }
    }
}
