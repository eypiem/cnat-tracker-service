package dev.apma.cnat.trackerservice.dto;


import dev.apma.cnat.trackerservice.model.Tracker;

/**
 * This DTO class represents the tracker data format returned from CNAT Tracker Service.
 *
 * @author Amir Parsa Mahdian
 */
public record TrackerDTO(String id, String userId, String name) {
    public static TrackerDTO fromTracker(Tracker t) {
        return new TrackerDTO(t.id(), t.userId(), t.name());
    }
}
