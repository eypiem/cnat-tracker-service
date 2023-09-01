package dev.apma.cnat.trackerservice.exception;


/**
 * This {@code Exception} indicates a situation where a not existing tracker has been requested.
 *
 * @author Amir Parsa Mahdian
 */
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
