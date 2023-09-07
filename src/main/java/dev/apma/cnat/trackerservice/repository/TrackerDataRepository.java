package dev.apma.cnat.trackerservice.repository;


import dev.apma.cnat.trackerservice.model.TrackerData;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

/**
 * This interface extends the {@code MongoRepository} interface and allows Spring Boot Data MongoDB to perform
 * operations of the <i>trackerData</i> collection.
 *
 * @author Amir Parsa Mahdian
 */
public interface TrackerDataRepository extends MongoRepository<TrackerData, String> {
    @Aggregation(pipeline = {"{ $match: { 'tracker': '?0' } }", "{ $sort: { 'timestamp': -1 } }", "{ $limit: ?1 }"})
    List<TrackerData> findAllByTrackerId(String trackerId, int limit);

    @Aggregation(pipeline = {"{ $match: { 'tracker': '?0', 'timestamp': { $gt: ?1 } } }",
            "{ $sort: { 'timestamp': 1 } }",
            "{ $limit: ?2 }"})
    List<TrackerData> findAllByTrackerIdAndDateAfter(String trackerId, Instant from, int limit);

    @Aggregation(pipeline = {"{ $match: { 'tracker': '?0', 'timestamp': { $lt: ?1 } } }",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: ?2 }"})
    List<TrackerData> findAllByTrackerIdAndDateBefore(String trackerId, Instant to, int limit);

    @Aggregation(pipeline = {"{ $match: { 'tracker': '?0', 'timestamp': {  $gt: ?1, $lt: ?2 } } }",
            "{ $sort: { 'timestamp': -1 } }"})
    List<TrackerData> findAllByTrackerIdAndDateAfterAndDateBefore(String trackerId,
                                                                  Instant from,
                                                                  Instant to,
                                                                  int limit);

    @Aggregation(pipeline = {"{ $match: { 'tracker': '?0' } }",
            "{ $match: {'data.coordinates': { $exists : true } } }",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: ?1 }"})
    List<TrackerData> findAllByTrackerIdWithCoordinates(String trackerId, int limit);

    @Aggregation(pipeline = {
            "{ $match: { 'tracker': '?0', 'timestamp': { $gt: ?1 }, 'data.coordinates': { $exists : true } } }",
            "{ $sort: { 'timestamp': 1 } }",
            "{ $limit: ?2 }"})
    List<TrackerData> findAllByTrackerIdAndDateAfterWithCoordinates(String trackerId, Instant from, int limit);

    @Aggregation(pipeline = {
            "{ $match: { 'tracker': '?0', 'timestamp': { $lt: ?1 }, 'data.coordinates': { $exists : true } } }",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: ?2 }"})
    List<TrackerData> findAllByTrackerIdAndDateBeforeWithCoordinates(String trackerId, Instant to, int limit);

    @Aggregation(pipeline = {
            "{ $match: { 'tracker': '?0', 'timestamp': {  $gt: ?1, $lt: ?2 }, 'data.coordinates': { $exists : true } "
            + "} }",
            "{ $sort: { 'timestamp': -1 } }"})
    List<TrackerData> findAllByTrackerIdAndDateAfterAndDateBeforeWithCoordinates(String trackerId,
                                                                                 Instant from,
                                                                                 Instant to,
                                                                                 int limit);

    @DeleteQuery(value = "{'tracker': '?0'}")
    void deleteAllByTrackerId(String trackerId);

    @DeleteQuery(value = "{'tracker': {$in: ?0}}")
    void deleteAllByTrackerIds(List<String> trackerIds);
}
