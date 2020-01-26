package view;

public class ViewConfigurationException extends RuntimeException {

    public ViewConfigurationException(String message) {
        super(message);
    }

    public ViewConfigurationException(Throwable cause) {
        super(cause);
    }

    public ViewConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
