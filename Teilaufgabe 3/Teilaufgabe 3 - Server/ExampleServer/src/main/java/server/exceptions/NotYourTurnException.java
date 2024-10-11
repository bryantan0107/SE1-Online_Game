package server.exceptions;

public class NotYourTurnException extends GenericExampleException {

	public NotYourTurnException(String errorMessage) {
		super("NotYourTurnException", errorMessage);
	}

}