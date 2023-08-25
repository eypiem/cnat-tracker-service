package dev.apma.cnat.trackerservice.requests;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

public record TrackerDataRegisterRequest(@NotNull(message = "Tracker must not be null") @Valid Tracker tracker,
                                         @NotNull(message = "Data must not be null") Map<String, Object> data,
                                         @NotNull(message = "Timestamp must not be null") Instant timestamp) {
    public record Tracker(@NotBlank(message = "Tracker's ID must not be blank") String id) {
    }
}
