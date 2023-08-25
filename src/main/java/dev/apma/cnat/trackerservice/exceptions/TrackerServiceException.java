package dev.apma.cnat.trackerservice.exceptions;


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
