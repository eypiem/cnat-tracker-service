package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.request.TrackerDataRegisterRequest;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of the {@code TrackerDataService} interface.
 *
 * @author Amir Parsa Mahdian
 * @see dev.apma.cnat.trackerservice.service.TrackerDataService
 */
@Service
@Transactional
public class TrackerDataServiceImpl implements TrackerDataService {

    /**
     * The default limit applied to <i>get tracker data</i> queries
     */
    private static final int DEFAULT_LIMIT = 10;

    /**
     * The tracker repository
     */
    private final TrackerRepository trackerRepo;

    /**
     * The tracker data repository
     */
    private final TrackerDataRepository trackerDataRepo;

    @Autowired
    public TrackerDataServiceImpl(TrackerRepository trackerRepo, TrackerDataRepository trackerDataRepo) {
        this.trackerRepo = trackerRepo;
        this.trackerDataRepo = trackerDataRepo;
    }

    @Override
    public void register(TrackerDataRegisterRequest req) throws TrackerDoesNotExistException {

        Optional<Tracker> t = trackerRepo.findById(req.tracker().id());
        if (t.isPresent()) {
            trackerDataRepo.save(new TrackerData(t.get(), req.data(), req.timestamp()));
        } else {
            throw new TrackerDoesNotExistException("Tracker with id [%s] does not exist.".formatted(req.tracker()
                    .id()));
        }
    }

    @Override
    public List<TrackerDataDTO> getTrackerData(String trackerId,
                                               @Nullable Instant from,
                                               @Nullable Instant to,
                                               @Nullable Boolean hasCoordinates,
                                               @Nullable Integer limit) {
        int lim = limit == null ? DEFAULT_LIMIT : limit;
        if (lim < 1) {
            return List.of();
        }
        boolean hasC = hasCoordinates != null && hasCoordinates;
        List<TrackerData> r;
        if (from != null && to != null) {
            r = hasC ? trackerDataRepo.findAllByTrackerIdAndDateAfterAndDateBeforeWithCoordinates(trackerId,
                    from,
                    to) : trackerDataRepo.findAllByTrackerIdAndDateAfterAndDateBefore(trackerId, from, to);
        } else if (from != null) {
            r = hasC
                    ? trackerDataRepo.findAllByTrackerIdAndDateAfterWithCoordinates(trackerId, from, lim)
                    : trackerDataRepo.findAllByTrackerIdAndDateAfter(trackerId, from, lim);
        } else if (to != null) {
            r = hasC
                    ? trackerDataRepo.findAllByTrackerIdAndDateBeforeWithCoordinates(trackerId, to, lim)
                    : trackerDataRepo.findAllByTrackerIdAndDateBefore(trackerId, to, lim);
        } else {
            r = hasC
                    ? trackerDataRepo.findAllByTrackerIdWithCoordinates(trackerId, lim)
                    : trackerDataRepo.findAllByTrackerId(trackerId, lim);
        }
        return r.stream().map(TrackerDataDTO::fromTrackerData).toList();
    }

    @Override
    public List<TrackerDataDTO> getLatestTrackerData(String userId) {
        return trackerRepo.findLatestTrackerDataWithCoordinatesByUserId(userId)
                .stream()
                .map(TrackerDataDTO::fromTrackerData)
                .toList();
    }
}
