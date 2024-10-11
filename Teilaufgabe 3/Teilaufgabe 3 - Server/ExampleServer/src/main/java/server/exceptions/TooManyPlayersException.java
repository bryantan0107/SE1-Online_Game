package server.exceptions;

public class TooManyPlayersException extends GenericExampleException {

	public TooManyPlayersException(String errorMessage) {
		super("TooManyPlayersException", errorMessage);
	}

}
