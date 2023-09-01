package dev.apma.cnat.trackerservice.dto;


import dev.apma.cnat.trackerservice.model.TrackerData;

import java.time.Instant;
import java.util.Map;

/**
 * This DTO class represents the tracker data format returned from CNAT Tracker Service.
 *
 * @author Amir Parsa Mahdian
 */
public record TrackerDataDTO(TrackerDTO tracker, Map<String, Object> data, Instant timestamp) {
    public static TrackerDataDTO fromTrackerData(TrackerData td) {
        return new TrackerDataDTO(TrackerDTO.fromTracker(td.tracker()), td.data(), td.timestamp());
    }
}
