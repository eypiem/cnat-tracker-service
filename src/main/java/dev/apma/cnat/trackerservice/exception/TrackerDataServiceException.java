package dev.apma.cnat.trackerservice.exception;


/**
 * This {@code Exception} indicates a situation where a request could not be performed on the {@code
 * TrackerDataService}.
 *
 * @author Amir Parsa Mahdian
 */
public class TrackerDataServiceException extends Exception {

    public TrackerDataServiceException() {
        super();
    }

    public TrackerDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackerDataServiceException(String message) {
        super(message);
    }

    public TrackerDataServiceException(Throwable cause) {
        super(cause);
    }
}
