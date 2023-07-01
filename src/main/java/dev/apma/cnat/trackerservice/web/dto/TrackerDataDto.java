package dev.apma.cnat.trackerservice.web.dto;


import java.time.Instant;
import java.util.Map;

public record TrackerDataDto(String trackerId, Map<String, Object> data, Instant timestamp) {
    @Override
    public String toString() {
        return "TrackerData{" + "trackerId=" + trackerId + ", data=" + data + ", timestamp=" + timestamp + '}';
    }
}
