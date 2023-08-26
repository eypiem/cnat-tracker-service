package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.MongoDBTestContainerConfig;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerDataRegisterRequest;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
class TrackerDataServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private TrackerService trackerSvc;

    @Autowired
    private TrackerDataService trackerDataSvc;

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void getLatest() throws TrackerDoesNotExistException {
        var trr1 = new TrackerRegisterRequest("1@test.com", "name1");
        var t1 = trackerSvc.registerTracker(trr1);

        var td1 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(t1.id()),
                Map.of("param1", 100),
                Instant.parse("2023-01-01T00:00:00.0Z"));
        var td2 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(t1.id()),
                Map.of("param1", 100),
                Instant.parse("2023-01-01T00:00:00.1Z"));
        var td3 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(t1.id()),
                Map.of("param1", 100),
                Instant.parse("2023-01-01T00:00:00.2Z"));
        trackerDataSvc.register(td1);
        trackerDataSvc.register(td3);
        trackerDataSvc.register(td2);

        var actual = trackerDataSvc.getTrackerData(t1.id(), td1.timestamp(), td3.timestamp(), null, null);
        assertEquals(1, actual.size());
        assertEquals(td2.timestamp(), actual.get(0).timestamp());
    }
}