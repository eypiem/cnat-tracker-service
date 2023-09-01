package dev.apma.cnat.trackerservice.exception;


/**
 * This {@code Exception} indicates a situation where a request could not be performed on the {@code TrackerService}.
 *
 * @author Amir Parsa Mahdian
 */
public class TrackerServiceException extends Exception {

    public TrackerServiceException() {
        super();
    }

    public TrackerServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackerServiceException(String message) {
        super(message);
    }

    public TrackerServiceException(Throwable cause) {
        super(cause);
    }
}
