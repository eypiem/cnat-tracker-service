package dev.apma.cnat.trackerservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents a <i>tracker</i> document stored in the {@code tracker} collection.
 *
 * @param id     ID of the tracker
 * @param userId userID of the tracker's owner
 * @param name   name of the tracker
 * @author Amir Parsa Mahdian
 */
@Document("tracker")
public record Tracker(@Id String id, @Indexed String userId, String name) {
}
