package dev.apma.cnat.trackerservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.apma.cnat.trackerservice.ControllerTestConfig;
import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;
import dev.apma.cnat.trackerservice.service.TrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackerRestController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = ControllerTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrackerRestControllerTest {
    public static final String USER_ATT = "userId";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static TrackerService trackerSvc;

    @Nested
    class RegisterTracker {

        private static final TrackerDTO t = new TrackerDTO("id", "user1", "name");

        @BeforeEach
        void setup() {
            when(trackerSvc.registerTracker(isA(TrackerRegisterRequest.class))).thenReturn(t);
        }

        @Test
        void valid() throws Exception {
            var req = new TrackerRegisterRequest(t.userId(), t.name());

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(req)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").exists())
                    .andExpect(jsonPath("userId").value(t.userId()))
                    .andExpect(jsonPath("name").value(t.name()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "0"})
        void valid_user_id(String userId) throws Exception {
            var req = new TrackerRegisterRequest(userId, t.name());

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        void invalid_user_id(String userId) throws Exception {
            var req = new TrackerRegisterRequest(userId, t.name());

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"aa", "12345678901234567890123456789012345678901234567890"})
        void valid_name(String name) throws Exception {
            var req = new TrackerRegisterRequest(t.userId(), name);

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "a", "123456789012345678901234567890123456789012345678901"})
        void invalid_name(String name) throws Exception {
            var req = new TrackerRegisterRequest(t.userId(), name);

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isBadRequest());
        }

        @Test
        void invalid_no_body() throws Exception {
            mockMvc.perform(post("")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetUserTrackers {
        public static final String USER_ID = "user1";
        private static final TrackerDTO t1 = new TrackerDTO("id1", USER_ID, "name1");
        private static final TrackerDTO t2 = new TrackerDTO("id2", USER_ID, "name2");

        @BeforeEach
        void setup() {
            when(trackerSvc.getUserTrackers(isA(String.class))).thenReturn(List.of(t1, t2));
        }

        @Test
        void valid() throws Exception {

            mockMvc.perform(get("").param(USER_ATT, USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(t1.id()))
                    .andExpect(jsonPath("$[1].id").value(t2.id()));
        }

        @Test
        void invalid_no_param() throws Exception {
            mockMvc.perform(get("")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetTracker {
        public static final String USER_ID = "user1";
        private static final TrackerDTO t1 = new TrackerDTO("id", USER_ID, "name");

        @BeforeEach
        void setup() throws TrackerDoesNotExistException {
            when(trackerSvc.getTrackerById(isA(String.class))).thenReturn(t1);
        }

        @Test
        void valid() throws Exception {
            mockMvc.perform(get("/" + t1.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(t1.id()))
                    .andExpect(jsonPath("userId").value(t1.userId()))
                    .andExpect(jsonPath("name").value(t1.name()));
        }

        @Test
        void non_existent_tracker() throws Exception {
            when(trackerSvc.getTrackerById(eq("non-existent"))).thenThrow(TrackerDoesNotExistException.class);
            mockMvc.perform(get("/non-existent")).andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteTracker {
        public static final String USER_ID = "user1";
        private static final TrackerDTO t1 = new TrackerDTO("id", USER_ID, "name");

        @BeforeEach
        void setup() {
            doNothing().when(trackerSvc).deleteTrackerById(isA(String.class));
        }

        @Test
        void valid() throws Exception {
            mockMvc.perform(delete("/" + t1.id())).andExpect(status().isOk());
        }
    }

    @Nested
    class DeleteAllUserTrackers {
        public static final String USER_ID = "user1";

        @BeforeEach
        void setup() {
            doNothing().when(trackerSvc).deleteAllUserTrackers(isA(String.class));
        }

        @Test
        void valid() throws Exception {
            mockMvc.perform(delete("").param(USER_ATT, USER_ID)).andExpect(status().isOk());
        }

        @Test
        void invalid_no_param() throws Exception {
            mockMvc.perform(delete("")).andExpect(status().isBadRequest());
        }
    }
}
