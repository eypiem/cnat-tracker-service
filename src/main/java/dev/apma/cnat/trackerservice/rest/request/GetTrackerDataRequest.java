package dev.apma.cnat.trackerservice.rest.request;


import dev.apma.cnat.trackerservice.model.Tracker;

import java.time.Instant;
import java.util.Optional;

public record GetTrackerDataRequest(Tracker tracker, Optional<Instant> from, Optional<Instant> to) {
}
