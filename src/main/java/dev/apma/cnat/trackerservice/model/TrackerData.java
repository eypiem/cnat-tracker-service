package dev.apma.cnat.trackerservice.model;


import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * This class represents a <i>tracker data</i> document stored in the {@code trackerData} collection.
 *
 * @param tracker   the tracker this tracker data belongs to
 * @param data      the payload of the reading
 * @param timestamp the timestamp of the reading
 * @author Amir Parsa Mahdian
 */
@Document("trackerData")
@CompoundIndex(def = "{ 'tracker': 1, 'timestamp': 1 }")
@CompoundIndex(def = "{ 'tracker.$id': 1, timestamp: -1 }")
public record TrackerData(@DBRef Tracker tracker, Map<String, Object> data, @Indexed Instant timestamp) {
}
