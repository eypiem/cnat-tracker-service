package dev.apma.cnat.trackerservice.model;


import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document("trackerData")
public record TrackerData(@DBRef Tracker tracker, Map<String, Object> data, Instant timestamp) {
    @Override
    public String toString() {
        return "TrackerData{" + "tracker=" + tracker + ", data=" + data + ", timestamp=" + timestamp + '}';
    }
}
