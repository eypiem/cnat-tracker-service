package dev.apma.cnat.trackerservice.dto;


import java.time.Instant;
import java.util.Map;

public record TrackerDataDto(String trackerId, Map<String, Object> data, Instant timestamp) {
}
