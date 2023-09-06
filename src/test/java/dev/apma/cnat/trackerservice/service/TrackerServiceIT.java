package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.service.MongoDBTestContainerConfig;
import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrackerServiceIT {

    @Autowired
    private TrackerService trackerSvc;

    @Autowired
    private TrackerDataService trackerDataSvc;


    private static final String USER_1 = "1@test.com";
    private static final String USER_2 = "2@test.com";
    private static final String USER_3 = "3@test.com";
    private static final String USER_4 = "4@test.com";

    private static final String NON_EXISTENT_TRACKER_ID = UUID.randomUUID().toString();

    // NOTE: naming represents user_tracker
    private TrackerDTO u1_t1;
    private TrackerDTO u1_t2;
    private TrackerDTO u2_t1;
    private TrackerDTO u3_t1;

    @BeforeAll
    void setup() {
        // NOTE: naming represents user_tracker
        u1_t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_1, "name11"));
        u1_t2 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_1, "name12"));
        u2_t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_2, "name21"));
        u3_t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(USER_3, "name31"));
    }

    @AfterAll
    void tearDown() {
        trackerSvc.deleteAllUserTrackers(USER_1);
        trackerSvc.deleteAllUserTrackers(USER_2);
        trackerSvc.deleteAllUserTrackers(USER_3);
        trackerSvc.deleteAllUserTrackers(USER_4);
    }

    @Nested
    class RegisterTracker {

        @Test
        void valid() {
            var req = new TrackerRegisterRequest(UUID.randomUUID().toString(), "name1");
            var t = trackerSvc.registerTracker(req);

            assertAll(() -> assertEquals(req.userId(), t.userId()),
                    () -> assertEquals(req.name(), t.name()),
                    () -> assertTrue(t.id().length() > 0));
        }
    }

    @Nested
    class GetTrackers {

        @Test
        void tracker_1() throws TrackerDoesNotExistException {
            var actual = trackerSvc.getTrackerById(u1_t1.id());

            assertAll(() -> assertEquals(u1_t1.id(), actual.id()),
                    () -> assertEquals(u1_t1.userId(), actual.userId()),
                    () -> assertEquals(u1_t1.name(), actual.name()));
        }

        @Test
        void non_existent_tracker() {
            assertThrows(TrackerDoesNotExistException.class, () -> trackerSvc.getTrackerById(NON_EXISTENT_TRACKER_ID));
        }
    }

    @Nested
    class GetUserTrackers {

        @Test
        void user_1() {
            var actual = trackerSvc.getUserTrackers(USER_1);

            assertAll(() -> assertEquals(2, actual.size()),
                    () -> assertTrue(actual.stream().anyMatch(e -> u1_t1.id().equals(e.id()))),
                    () -> assertTrue(actual.stream().anyMatch(e -> u1_t2.id().equals(e.id()))));
        }

        @Test
        void user_2() {
            var actual = trackerSvc.getUserTrackers(USER_2);

            assertAll(() -> assertEquals(1, actual.size()),
                    () -> assertTrue(actual.stream().anyMatch(e -> u2_t1.id().equals(e.id()))));
        }

        @Test
        void user_with_no_trackers() {
            var actual = trackerSvc.getUserTrackers(USER_4);

            assertAll(() -> assertEquals(0, actual.size()));
        }

        @Test
        void non_existent_user() {
            var actual = trackerSvc.getUserTrackers(NON_EXISTENT_TRACKER_ID);

            assertAll(() -> assertEquals(0, actual.size()));
        }
    }

    @Nested
    class DeleteTracker {

        @Test
        void valid() {
            var t = trackerSvc.registerTracker(new TrackerRegisterRequest(UUID.randomUUID().toString(), "name1"));
            trackerSvc.deleteTrackerById(t.id());

            assertAll(() -> assertThrows(TrackerDoesNotExistException.class, () -> trackerSvc.getTrackerById(t.id())),
                    () -> assertEquals(0, trackerDataSvc.getTrackerData(t.id(), null, null, null, null).size()));
        }

        @Test
        void non_existent_tracker() {
            assertDoesNotThrow(() -> trackerSvc.deleteTrackerById(NON_EXISTENT_TRACKER_ID));
        }
    }

    @Nested
    class DeleteAllUserTrackers {

        @Test
        void valid() {
            var userId = UUID.randomUUID().toString();
            var t1 = trackerSvc.registerTracker(new TrackerRegisterRequest(userId, "name1"));
            var t2 = trackerSvc.registerTracker(new TrackerRegisterRequest(userId, "name2"));
            trackerSvc.deleteAllUserTrackers(userId);

            assertAll(() -> assertEquals(0, trackerSvc.getUserTrackers(userId).size()),
                    () -> assertEquals(0, trackerDataSvc.getTrackerData(t1.id(), null, null, null, null).size()),
                    () -> assertEquals(0, trackerDataSvc.getTrackerData(t2.id(), null, null, null, null).size()));
        }

        @Test
        void non_existent_user() {
            assertDoesNotThrow(() -> trackerSvc.deleteAllUserTrackers(UUID.randomUUID().toString()));
        }
    }
}
