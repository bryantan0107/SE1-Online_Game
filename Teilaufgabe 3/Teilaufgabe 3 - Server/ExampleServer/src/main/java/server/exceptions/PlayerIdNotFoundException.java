package server.exceptions;

public class PlayerIdNotFoundException extends GenericExampleException {

	public PlayerIdNotFoundException(String errorMessage) {
		super("PlayerIdNotFoundException", errorMessage);
	}

}