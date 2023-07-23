package dev.apma.cnat.trackerservice.repository;


import dev.apma.cnat.trackerservice.MongoDBTestContainerConfig;
import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
class TrackerDataRestControllerTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @Autowired
    private TrackerRepository trackerRepo;

    @AfterEach
    public void tearDown() {
        trackerDataRepo.deleteAll();
    }

    @Test
    public void getLatest() {
        Tracker t1 = new Tracker("1", "1@test.com", "name1");
        trackerRepo.save(t1);
        TrackerData td1 = new TrackerData(t1, Map.of("param1", 100), Instant.parse("2023-01-01T00:00:00.0Z"));
        TrackerData td2 = new TrackerData(t1, Map.of("param1", 80), Instant.parse("2023-01-01T00:00:00.2Z"));
        TrackerData td3 = new TrackerData(t1, Map.of("param1", 120), Instant.parse("2023-01-01T00:00:00.1Z"));
        trackerDataRepo.save(td1);
        trackerDataRepo.save(td2);
        trackerDataRepo.save(td3);

        Optional<TrackerData> actual = trackerDataRepo.findLatestByTrackerId(t1.id());
        assertTrue(actual.isPresent());
        assertEquals(td2, actual.get());
    }
}
