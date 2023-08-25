package dev.apma.cnat.trackerservice.requests;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

public record TrackerDataRegisterRequest(@NotNull(message = "Key [tracker] must not be null") @Valid Tracker tracker,
                                         @NotNull(message = "Key [data] must not be null") Map<String, Object> data,
                                         @NotNull(message = "Key [timestamp] must not be null") Instant timestamp) {
    public record Tracker(@NotBlank(message = "Key [tracker.id] must not be blank") String id) {
    }
}
