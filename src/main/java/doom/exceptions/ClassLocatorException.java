package doom.exceptions;

public class ClassLocatorException extends RuntimeException{
    public ClassLocatorException(String message) {
        super(message);
    }

    public ClassLocatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
