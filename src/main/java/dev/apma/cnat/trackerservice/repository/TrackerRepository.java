package dev.apma.cnat.trackerservice.repository;


import dev.apma.cnat.trackerservice.model.Tracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TrackerRepository extends MongoRepository<Tracker, String> {
    @Query("{userId: '?0'}")
    List<Tracker> findAllByUserId(String userId);
}
