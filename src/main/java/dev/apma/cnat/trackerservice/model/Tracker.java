package dev.apma.cnat.trackerservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tracker")
public record Tracker(@Id String id, String userId) {
}
