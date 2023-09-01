package dev.apma.cnat.trackerservice.model;


import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document("trackerData")
@CompoundIndex(def = "{ 'tracker': 1, 'timestamp': 1 }")
@CompoundIndex(def = "{ 'tracker.$id': 1, timestamp: -1 }")
public record TrackerData(@DBRef Tracker tracker, Map<String, Object> data, @Indexed Instant timestamp) {
}
