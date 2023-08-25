package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exceptions.TrackerServiceException;
import dev.apma.cnat.trackerservice.requests.TrackerRegisterRequest;

import java.util.List;

public interface TrackerService {

    TrackerDTO registerTracker(TrackerRegisterRequest trr);

    List<TrackerDTO> getUserTrackers(String userId);

    TrackerDTO getTracker(String trackerId) throws TrackerServiceException;

    void deleteTracker(String trackerId);

    void deleteAllUserTrackers(String userId);
}
