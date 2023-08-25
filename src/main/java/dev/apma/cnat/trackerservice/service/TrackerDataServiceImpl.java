package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
import dev.apma.cnat.trackerservice.exceptions.TrackerDataServiceException;
import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.requests.TrackerDataRegisterRequest;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrackerDataServiceImpl implements TrackerDataService {
    private static final int DEFAULT_LIMIT = 10;

    private final TrackerRepository trackerRepo;

    private final TrackerDataRepository trackerDataRepo;

    @Autowired
    public TrackerDataServiceImpl(TrackerRepository trackerRepo, TrackerDataRepository trackerDataRepo) {
        this.trackerRepo = trackerRepo;
        this.trackerDataRepo = trackerDataRepo;
    }

    @Override
    public void register(TrackerDataRegisterRequest req) throws TrackerDataServiceException {

        Optional<Tracker> t = trackerRepo.findById(req.tracker().id());
        if (t.isPresent()) {
            trackerDataRepo.save(new TrackerData(t.get(), req.data(), req.timestamp()));
        } else {
            throw new TrackerDataServiceException("Tracker with id [%s] does not exist.".formatted(req.tracker().id()));
        }
    }

    @Override
    public List<TrackerDataDTO> getLatestTrackerData(String userId) {
        return trackerRepo.findLatestTrackerDataWithCoordinatesByUserId(userId)
                .stream()
                .map(TrackerDataDTO::fromTrackerData)
                .toList();
    }

    @Override
    public List<TrackerDataDTO> getTrackerData(String trackerId,
                                               @Nullable Instant from,
                                               @Nullable Instant to,
                                               @Nullable Boolean hasLocation,
                                               @Nullable Integer limit) {
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        List<TrackerData> r;
        if (hasLocation != null && hasLocation) {
            r = trackerDataRepo.findAllByTrackerIdWithCoordinates(trackerId, limit);
        } else if (from != null && to != null) {
            r = trackerDataRepo.findAllByTrackerIdAndDateAfterAndDateBefore(trackerId, from, to, limit);
        } else if (from != null) {
            r = trackerDataRepo.findAllByTrackerIdAndDateAfter(trackerId, from, limit);
        } else if (to != null) {
            r = trackerDataRepo.findAllByTrackerIdAndDateBefore(trackerId, to, limit);
        } else {
            r = trackerDataRepo.findAllByTrackerId(trackerId, limit);
        }
        return r.stream().map(TrackerDataDTO::fromTrackerData).toList();
    }
}
