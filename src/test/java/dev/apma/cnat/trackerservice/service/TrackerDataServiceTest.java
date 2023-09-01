package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.MongoDBTestContainerConfig;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerDataRegisterRequest;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrackerDataServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TrackerService trackerSvc;

    @Autowired
    private TrackerDataService trackerDataSvc;


    private static final String USER_1 = "1@test.com";
    private static final String USER_2 = "2@test.com";
    private static final String USER_3 = "3@test.com";
    private static final String USER_4 = "4@test.com";

    private static final String PARAM_1 = "param1";
    private static final String COORDINATES = "coordinates";

    // NOTE: naming represents user_tracker_data
    private TrackerDataRegisterRequest u1_t1_d1;
    private TrackerDataRegisterRequest u1_t1_d2;
    private TrackerDataRegisterRequest u1_t1_d3;
    private TrackerDataRegisterRequest u1_t2_d1;
    private TrackerDataRegisterRequest u2_t1_d1;

    @BeforeAll
    void setup() throws TrackerDoesNotExistException {
        // NOTE: naming represents user_tracker
        var u1_t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_1, "name11"));
        var u1_t2 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_1, "name12"));
        var u2_t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_2, "name21"));
        var u3_t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_2, "name31"));

        u1_t1_d1 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(u1_t1.id()),
                Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                Instant.parse("2023-01-01T00:00:00.0Z"));
        u1_t1_d2 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(u1_t1.id()),
                Map.of(PARAM_1, 112, COORDINATES, new Double[]{0.0, 0.0}),
                Instant.parse("2023-01-01T00:00:00.1Z"));
        u1_t1_d3 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(u1_t1.id()),
                Map.of(PARAM_1, 113),
                Instant.parse("2023-01-01T00:00:00.2Z"));
        u1_t2_d1 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(u1_t2.id()),
                Map.of(PARAM_1, 121, COORDINATES, new Double[]{0.0, 0.0}),
                Instant.parse("2023-01-01T00:00:00.2Z"));
        u2_t1_d1 = new TrackerDataRegisterRequest(new TrackerDataRegisterRequest.Tracker(u2_t1.id()),
                Map.of(PARAM_1, 211, COORDINATES, new Double[]{0.0, 0.0}),
                Instant.parse("2023-01-01T00:00:00.2Z"));

        trackerDataSvc.register(u1_t1_d1);
        trackerDataSvc.register(u1_t1_d2);
        trackerDataSvc.register(u1_t1_d3);
        trackerDataSvc.register(u1_t2_d1);
        trackerDataSvc.register(u2_t1_d1);
    }

    @AfterAll
    void tearDown() {
        u1_t1_d1 = null;
        u1_t1_d2 = null;
        u1_t2_d1 = null;
        u2_t1_d1 = null;
    }

    @Nested
    class GetLatestTrackerData {

        @Test
        void user_with_tracker_data_1() {
            var actual = trackerDataSvc.getLatestTrackerData(USER_1);
            assertAll(() -> assertEquals(2, actual.size()),
                    () -> assertTrue(actual.stream()
                            .anyMatch(e -> e.data().get(PARAM_1).equals(u1_t1_d2.data().get(PARAM_1)))),
                    () -> assertTrue(actual.stream()
                            .anyMatch(e -> e.data().get(PARAM_1).equals(u1_t2_d1.data().get(PARAM_1)))));
        }

        @Test
        void user_with_tracker_data_2() {
            var actual = trackerDataSvc.getLatestTrackerData(USER_2);
            assertAll(() -> assertEquals(1, actual.size()),
                    () -> assertTrue(actual.stream()
                            .anyMatch(e -> e.data().get(PARAM_1).equals(u2_t1_d1.data().get(PARAM_1)))));
        }

        @Test
        void user_with_tracker_and_no_tracker_data() {
            var actual = trackerDataSvc.getLatestTrackerData(USER_3);
            assertEquals(0, actual.size());
        }

        @Test
        void user_with_no_tracker() {
            var actual = trackerDataSvc.getLatestTrackerData(USER_4);
            assertEquals(0, actual.size());
        }
    }

    @Nested
    class GetTrackerData {

        @Test
        void from() {
            var actual = trackerDataSvc.getTrackerData(u1_t1_d1.tracker().id(), u1_t1_d1.timestamp(), null, null, null);

            assertAll(() -> assertEquals(2, actual.size()),
                    () -> assertTrue(actual.stream().anyMatch(e -> e.timestamp().equals(u1_t1_d2.timestamp()))),
                    () -> assertTrue(actual.stream().anyMatch(e -> e.timestamp().equals(u1_t1_d3.timestamp()))));
        }

        @Test
        void to() {
            var actual = trackerDataSvc.getTrackerData(u1_t1_d1.tracker().id(), null, u1_t1_d3.timestamp(), null, null);

            assertAll(() -> assertEquals(2, actual.size()),
                    () -> assertTrue(actual.stream().anyMatch(e -> e.timestamp().equals(u1_t1_d1.timestamp()))),
                    () -> assertTrue(actual.stream().anyMatch(e -> e.timestamp().equals(u1_t1_d2.timestamp()))));
        }

        @Test
        void from_to() {
            var actual = trackerDataSvc.getTrackerData(u1_t1_d1.tracker().id(),
                    u1_t1_d1.timestamp(),
                    u1_t1_d3.timestamp(),
                    null,
                    null);

            assertAll(() -> assertEquals(1, actual.size()),
                    () -> assertTrue(actual.stream()
                            .anyMatch(e -> e.data().get(PARAM_1).equals(u1_t1_d2.data().get(PARAM_1)))));
        }

        @Test
        void from_limit() {
            var actual = trackerDataSvc.getTrackerData(u1_t1_d1.tracker().id(), u1_t1_d1.timestamp(), null, null, 1);

            assertAll(() -> assertEquals(1, actual.size()),
                    () -> assertTrue(actual.stream()
                            .anyMatch(e -> e.data().get(PARAM_1).equals(u1_t1_d2.data().get(PARAM_1)))));
        }

        @Test
        void to_limit() {
            var actual = trackerDataSvc.getTrackerData(u1_t1_d1.tracker().id(), null, u1_t1_d3.timestamp(), null, 1);

            assertAll(() -> assertEquals(1, actual.size()),
                    () -> assertTrue(actual.stream()
                            .anyMatch(e -> e.data().get(PARAM_1).equals(u1_t1_d1.data().get(PARAM_1)))));
        }
    }
}