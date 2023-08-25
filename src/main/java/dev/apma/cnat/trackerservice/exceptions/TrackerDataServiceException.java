package dev.apma.cnat.trackerservice.exceptions;


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
