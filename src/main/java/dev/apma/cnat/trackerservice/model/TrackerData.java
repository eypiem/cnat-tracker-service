package dev.apma.cnat.trackerservice.model;


import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document("trackerData")
@CompoundIndex(name = "tracker.$id_idx", def = "{ 'tracker.$id': 1 }")
@CompoundIndex(name = "tracker.$id_timestamp_idx", def = "{ 'tracker.$id': 1, timestamp: -1 }")
public record TrackerData(@DBRef Tracker tracker, Map<String, Object> data, Instant timestamp) {
}
