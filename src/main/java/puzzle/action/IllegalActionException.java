package puzzle.action;

public class IllegalActionException extends RuntimeException {

    public IllegalActionException(String message) {
        super(message);
    }

    public IllegalActionException(Throwable cause) {
        super(cause);
    }

    public IllegalActionException(String message, Throwable cause) {
        super(message, cause);
    }

}
