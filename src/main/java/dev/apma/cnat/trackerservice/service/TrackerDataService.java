package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerDataRegisterRequest;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.List;


public interface TrackerDataService {

    void register(TrackerDataRegisterRequest req) throws TrackerDoesNotExistException;

    List<TrackerDataDTO> getLatestTrackerData(String userId);

    List<TrackerDataDTO> getTrackerData(String trackerId,
                                        @Nullable Instant from,
                                        @Nullable Instant to,
                                        @Nullable Boolean hasCoordinates,
                                        @Nullable Integer limit);
}
