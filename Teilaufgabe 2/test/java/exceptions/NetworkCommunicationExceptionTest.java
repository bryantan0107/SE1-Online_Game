package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NetworkCommunicationExceptionTest {

    @Test
    void GivenErrorMessage_ThrowingCorrectNetworkCommunicationException() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        NetworkCommunicationException exception = new NetworkCommunicationException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void GivenErrorMessage_ThrowingCorrectGetErrorMessage() {
        // Arrange
        String errorMessage = "Test error message";
        NetworkCommunicationException exception = new NetworkCommunicationException(errorMessage);

        // Act

        // Assert
    }
}
