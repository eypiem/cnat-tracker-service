package dev.apma.cnat.trackerservice.repository;


import dev.apma.cnat.trackerservice.model.TrackerData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface TrackerDataRepository extends MongoRepository<TrackerData, String> {
    @Query(value = "{tracker:'?0'}")
    List<TrackerData> findAll(String trackerId);

    @Query(value = "{tracker:'?0', 'timestamp' : { $gt: ?1 }}")
    List<TrackerData> findAllByDateAfter(String trackerId, Instant from);

    @Query(value = "{tracker:'?0', 'timestamp' : { $lt: ?1 }}")
    List<TrackerData> findAllByDateBefore(String trackerId, Instant to);

    @Query(value = "{tracker:'?0', 'timestamp' : { $gt: ?1, $lt: ?2 }}")
    List<TrackerData> findAllByDateAfterAndDateBefore(String trackerId, Instant from, Instant to);
}
