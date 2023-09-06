package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.ControllerTestConfig;
import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
import dev.apma.cnat.trackerservice.service.TrackerDataService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TrackerDataRestController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = ControllerTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrackerDataRestControllerTest {
    private static final String PARAM_1 = "param1";
    private static final String COORDINATES = "coordinates";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static TrackerDataService trackerDataSvc;

    @Nested
    class GetTrackerData {
        private static final String FROM_ATT = "from";
        private static final String TO_ATT = "to";
        private static final String HAS_COORDINATES_ATT = "hasCoordinates";
        private static final String LIMIT_ATT = "limit";

        private static final String USER_ID = "test1@test.com";
        private static final String TRACKER_ID = "1";
        private static TrackerDataDTO td1;

        @Test
        void valid() throws Exception {
            td1 = new TrackerDataDTO(new TrackerDTO(TRACKER_ID, USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.0Z"));
            when(trackerDataSvc.getTrackerData(eq(TRACKER_ID),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull())).thenReturn(List.of(td1));
            mockMvc.perform(get("/" + TRACKER_ID + "/data"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }

        @Test
        void from() throws Exception {
            td1 = new TrackerDataDTO(new TrackerDTO(TRACKER_ID, USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.1Z"));
            when(trackerDataSvc.getTrackerData(eq(TRACKER_ID),
                    isA(Instant.class),
                    isNull(),
                    isNull(),
                    isNull())).thenReturn(List.of(td1));
            mockMvc.perform(get("/" + TRACKER_ID + "/data?" + FROM_ATT + "=2023-01-01T00:00:00.0Z"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }

        @Test
        void to() throws Exception {
            td1 = new TrackerDataDTO(new TrackerDTO(TRACKER_ID, USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.0Z"));
            when(trackerDataSvc.getTrackerData(eq(TRACKER_ID),
                    isNull(),
                    isA(Instant.class),
                    isNull(),
                    isNull())).thenReturn(List.of(td1));
            mockMvc.perform(get("/" + TRACKER_ID + "/data?" + TO_ATT + "=2023-01-01T00:00:00.1Z"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }

        @Test
        void from_to() throws Exception {
            td1 = new TrackerDataDTO(new TrackerDTO(TRACKER_ID, USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.1Z"));
            when(trackerDataSvc.getTrackerData(eq(TRACKER_ID),
                    isA(Instant.class),
                    isA(Instant.class),
                    isNull(),
                    isNull())).thenReturn(List.of(td1));
            mockMvc.perform(get("/"
                                + TRACKER_ID
                                + "/data?"
                                + FROM_ATT
                                + "=2023-01-01T00:00:00.0Z&"
                                + TO_ATT
                                + "=2023-01-01T00:00:00.2Z"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }

        @Test
        void from_to_has_tracker() throws Exception {
            td1 = new TrackerDataDTO(new TrackerDTO(TRACKER_ID, USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.1Z"));
            when(trackerDataSvc.getTrackerData(eq(TRACKER_ID),
                    isA(Instant.class),
                    isA(Instant.class),
                    isA(Boolean.class),
                    isNull())).thenReturn(List.of(td1));
            mockMvc.perform(get("/"
                                + TRACKER_ID
                                + "/data?"
                                + FROM_ATT
                                + "=2023-01-01T00:00:00.0Z&"
                                + TO_ATT
                                + "=2023-01-01T00:00:00.2Z&"
                                + HAS_COORDINATES_ATT
                                + "=true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }

        @Test
        void from_to_has_tracker_limit() throws Exception {
            td1 = new TrackerDataDTO(new TrackerDTO(TRACKER_ID, USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.1Z"));
            when(trackerDataSvc.getTrackerData(eq(TRACKER_ID),
                    isA(Instant.class),
                    isA(Instant.class),
                    isA(Boolean.class),
                    isA(Integer.class))).thenReturn(List.of(td1));
            mockMvc.perform(get("/"
                                + TRACKER_ID
                                + "/data?"
                                + FROM_ATT
                                + "=2023-01-01T00:00:00.0Z&"
                                + TO_ATT
                                + "=2023-01-01T00:00:00.2Z&"
                                + HAS_COORDINATES_ATT
                                + "=true&"
                                + LIMIT_ATT
                                + "=1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }
    }

    @Nested
    class GetLatestTrackerData {
        private static final String USER_ID = "test1@test.com";

        @Test
        void valid() throws Exception {
            var td1 = new TrackerDataDTO(new TrackerDTO("id", USER_ID, "name1"),
                    Map.of(PARAM_1, 111, COORDINATES, new Double[]{0.0, 0.0}),
                    Instant.parse("2023-01-01T00:00:00.0Z"));
            when(trackerDataSvc.getLatestTrackerData(isA(String.class))).thenReturn(List.of(td1));
            mockMvc.perform(get("/data/latest").param("userId", USER_ID))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].data.param1").value(td1.data().get(PARAM_1)))
                    .andExpect(jsonPath("$[0].timestamp").value(td1.timestamp().toString()));
        }

        @Test
        void invalid_no_param() throws Exception {
            mockMvc.perform(get("/data/latest")).andExpect(status().isBadRequest());
        }
    }
}
