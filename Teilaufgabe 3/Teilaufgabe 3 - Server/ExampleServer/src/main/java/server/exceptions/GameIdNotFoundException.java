package server.exceptions;

public class GameIdNotFoundException extends GenericExampleException {

	public GameIdNotFoundException(String errorMessage) {
		super("GameIdNotFoundException", errorMessage);
	}

}
