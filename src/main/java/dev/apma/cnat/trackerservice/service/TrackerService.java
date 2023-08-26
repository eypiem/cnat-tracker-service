package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;

import java.util.List;

public interface TrackerService {

    TrackerDTO registerTracker(TrackerRegisterRequest trr);

    List<TrackerDTO> getUserTrackers(String userId);

    TrackerDTO getTracker(String trackerId) throws TrackerDoesNotExistException;

    void deleteTracker(String trackerId);

    void deleteAllUserTrackers(String userId);
}
