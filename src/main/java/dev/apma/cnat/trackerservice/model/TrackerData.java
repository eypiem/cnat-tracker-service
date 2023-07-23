package dev.apma.cnat.trackerservice.model;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document("trackerData")
public record TrackerData(@DBRef Tracker tracker, Map<String, Object> data, @Indexed Instant timestamp) {
}
