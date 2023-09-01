package dev.apma.cnat.trackerservice.service;


import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrackerServiceImpl implements TrackerService {

    private final TrackerDataRepository trackerDataRepo;

    private final TrackerRepository trackerRepo;

    @Autowired
    public TrackerServiceImpl(TrackerRepository trackerRepo, TrackerDataRepository trackerDataRepo) {
        this.trackerRepo = trackerRepo;
        this.trackerDataRepo = trackerDataRepo;
    }

    @Override
    public TrackerDTO registerTracker(TrackerRegisterRequest trr) {
        return TrackerDTO.fromTracker(trackerRepo.save(new Tracker(null, trr.userId(), trr.name())));
    }

    @Override
    public List<TrackerDTO> getUserTrackers(String userId) {
        return trackerRepo.findAllByUserId(userId).stream().map(TrackerDTO::fromTracker).toList();
    }

    @Override
    public TrackerDTO getTracker(String trackerId) throws TrackerDoesNotExistException {
        return TrackerDTO.fromTracker(trackerRepo.findById(trackerId)
                .orElseThrow(() -> new TrackerDoesNotExistException("Tracker with id [%s] does not exist".formatted(
                        trackerId))));
    }

    @Override
    public void deleteTracker(String trackerId) {
        trackerDataRepo.deleteAllByTrackerId(trackerId);
        trackerRepo.deleteById(trackerId);
    }

    @Override
    public void deleteAllUserTrackers(String userId) {
        List<String> trackerIdsToDelete = trackerRepo.findAllByUserId(userId).stream().map(Tracker::id).toList();
        trackerDataRepo.deleteAllByTrackerIds(trackerIdsToDelete);
        trackerRepo.deleteTrackersByUserId(userId);
    }
}
