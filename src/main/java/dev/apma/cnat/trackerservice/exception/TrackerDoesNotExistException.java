package dev.apma.cnat.trackerservice.exception;


public class TrackerDoesNotExistException extends TrackerServiceException {

    public TrackerDoesNotExistException() {
        super();
    }

    public TrackerDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackerDoesNotExistException(String message) {
        super(message);
    }

    public TrackerDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
