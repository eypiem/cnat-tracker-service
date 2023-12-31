package dev.apma.cnat.trackerservice.repository;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * This interface extends the {@code MongoRepository} interface and allows Spring Boot Data MongoDB to perform
 * operations of the <i>tracker</i> collection.
 *
 * @author Amir Parsa Mahdian
 */
public interface TrackerRepository extends MongoRepository<Tracker, String> {
    @Query("{'userId': '?0'}")
    List<Tracker> findAllByUserId(String userId);

    @Aggregation(pipeline = {"{ $match: { 'userId': '?0' } }",
            "{ $lookup: { from: 'trackerData', localField: '_id', foreignField: 'tracker.$id', as: 'trackerData', "
            + "pipeline: [ { $match: { 'data.coordinates': { $exists: true } } }, { $sort: { 'timestamp': -1 } }, { "
            + "$limit: 1 }] }}",
            "{ $match: {'trackerData': { $exists : true, $not: {$size: 0} } } }",
            "{ $project: { 'trackerData': { $first: '$trackerData' } } }",
            "{ $replaceRoot: { newRoot: '$trackerData' } }"})
    List<TrackerData> findLatestTrackerDataWithCoordinatesByUserId(String userId);

    @DeleteQuery
    void deleteTrackersByUserId(String userId);
}
