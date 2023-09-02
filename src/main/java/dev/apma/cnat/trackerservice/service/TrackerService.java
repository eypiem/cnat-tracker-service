package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;

import java.util.List;

/**
 * This interface represents a service for performing operations on {@code TrackerRepository}.
 *
 * @author Amir Parsa Mahdian
 * @see dev.apma.cnat.trackerservice.repository.TrackerRepository
 */
public interface TrackerService {

    /**
     * Registers a new tracker.
     *
     * @param req the {@code TrackerRegisterRequest} to be registered
     * @return A {@code TrackerDTO} containing the new tracker information
     */
    TrackerDTO registerTracker(TrackerRegisterRequest req);

    /**
     * Returns the tracker having the provided ID.
     *
     * @param trackerId the tracker's ID
     * @return the tracker having the provided ID
     * @throws TrackerDoesNotExistException if a tracker with the provided ID does not exist
     */
    TrackerDTO getTrackerById(String trackerId) throws TrackerDoesNotExistException;

    /**
     * Returns all trackers associated with the provided userId.
     *
     * @param userId the user's ID to match trackers with
     * @return all trackers associated with the provided userId
     */
    List<TrackerDTO> getUserTrackers(String userId);

    /**
     * Deletes the tracker and all tracker data associated with it.
     *
     * @param trackerId the tracker's ID
     */
    void deleteTrackerById(String trackerId);

    /**
     * Deletes all the trackers associated with the provided userId and all tracker data associated with them.
     *
     * @param userId the user's ID to match trackers with
     */
    void deleteAllUserTrackers(String userId);
}
