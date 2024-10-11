package exceptions;

public class NetworkCommunicationException extends RuntimeException {
	public NetworkCommunicationException(String errorMessage) {
        super(errorMessage);
    }
}
