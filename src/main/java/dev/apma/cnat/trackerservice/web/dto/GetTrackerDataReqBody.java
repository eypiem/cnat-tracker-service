package dev.apma.cnat.trackerservice.web.dto;


public record GetTrackerDataReqBody(String trackerId, String from, String to) {
    @Override
    public String toString() {
        return "GetTrackerDataReqBody{" + "trackerId='" + trackerId + '\'' + ", from=" + from + ", to=" + to + '}';
    }
}
