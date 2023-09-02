package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerDataRegisterRequest;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.List;


/**
 * This interface represents a service for performing operations on {@code TrackerDataRepository}.
 *
 * @author Amir Parsa Mahdian
 * @see dev.apma.cnat.trackerservice.repository.TrackerDataRepository
 */
public interface TrackerDataService {

    /**
     * Registers a new tacker data.
     *
     * @param req the {@code TrackerDataRegisterRequest} to be registered
     * @throws TrackerDoesNotExistException if the request points to a non-existent tracker
     */
    void register(TrackerDataRegisterRequest req) throws TrackerDoesNotExistException;

    /**
     * Returns all tracker's data satisfying the provided parameters.
     *
     * @param trackerId      the tracker's ID
     * @param from           filter tracker data with Instant after
     * @param to             filter tracker data with Instant before
     * @param hasCoordinates filter tracker data having coordinates
     * @param limit          limit the number of results (if null, a default limit may be applied and if invalid, an
     *                       empty list will be returned)
     * @return all tracker's data satisfying the provided parameters
     */
    List<TrackerDataDTO> getTrackerData(String trackerId,
                                        @Nullable Instant from,
                                        @Nullable Instant to,
                                        @Nullable Boolean hasCoordinates,
                                        @Nullable Integer limit);

    /**
     * Returns the most recent tracker data with coordinates from each of the trackers associated with the provided
     * user ID.
     *
     * @param userId the user's ID to match trackers with
     * @return the most recent tracker data with coordinates from each of the trackers associated with the provided
     * user ID
     */
    List<TrackerDataDTO> getLatestTrackerData(String userId);
}
