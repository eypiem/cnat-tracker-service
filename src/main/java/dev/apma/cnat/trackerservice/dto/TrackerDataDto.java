package dev.apma.cnat.trackerservice.dto;


import java.time.Instant;
import java.util.Map;

public record TrackerDataDto(TrackerDTO tracker, Map<String, Object> data, Instant timestamp) {
    public record TrackerDTO(String id, String userId, String name) {}
}
