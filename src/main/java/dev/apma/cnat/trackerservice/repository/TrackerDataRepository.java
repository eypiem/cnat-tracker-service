package dev.apma.cnat.trackerservice.repository;


import dev.apma.cnat.trackerservice.model.TrackerData;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TrackerDataRepository extends MongoRepository<TrackerData, String> {
    @Query(value = "{'tracker': '?0'}")
    List<TrackerData> findAllByTrackerId(String trackerId);

    @Query(value = "{'tracker': '?0', 'timestamp': { $gt: '?1' }}")
    List<TrackerData> findAllByTrackerIdAndDateAfter(String trackerId, Instant from);

    @Query(value = "{'tracker': '?0', 'timestamp' : { $lt: '?1' }}")
    List<TrackerData> findAllByTrackerIdAndDateBefore(String trackerId, Instant to);

    @Query(value = "{'tracker': '?0', 'timestamp' : { $gt: '?1', $lt: '?2' }}")
    List<TrackerData> findAllByTrackerIdAndDateAfterAndDateBefore(String trackerId, Instant from, Instant to);

    @Aggregation(pipeline = {"{ $match: { 'tracker': '?0' } }", "{ $sort: { 'timestamp': -1 } }", "{ $limit: 1 }"})
    Optional<TrackerData> findLatestByTrackerId(String trackerId);

    @DeleteQuery(value = "{'tracker': ?0}")
    void deleteAllByTrackerId(String trackerId);
}
