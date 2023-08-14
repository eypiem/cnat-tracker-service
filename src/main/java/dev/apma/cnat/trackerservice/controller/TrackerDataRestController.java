package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class TrackerDataRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerDataRestController.class);

    private final TrackerRepository trackerRepo;

    private final TrackerDataRepository trackerDataRepo;

    @Autowired
    public TrackerDataRestController(TrackerRepository trackerRepo, TrackerDataRepository trackerDataRepo) {
        this.trackerRepo = trackerRepo;
        this.trackerDataRepo = trackerDataRepo;
    }

    @GetMapping("/data/latest")
    public List<TrackerData> getLatestTrackerData(@RequestParam String userId) {
        LOGGER.info("get /latest {}", userId);
        return trackerRepo.findAllByUserId(userId)
                .stream()
                .map(Tracker::id)
                .map(trackerDataRepo::findLatestByTrackerId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @GetMapping("/{trackerId}/data")
    public List<TrackerData> getTrackerData(@PathVariable String trackerId,
                                            @RequestParam Optional<Instant> from,
                                            @RequestParam Optional<Instant> to,
                                            @RequestParam Optional<Boolean> hasLocation) {
        LOGGER.info("get /{}/data from: {} to: {} hasLocation: {}", trackerId, from, to, hasLocation);
        if (hasLocation.orElse(false)) {
            return trackerDataRepo.findAllByTrackerIdWithLocation(trackerId);
        }
        if (from.isPresent() && to.isPresent()) {
            return trackerDataRepo.findAllByTrackerIdAndDateAfterAndDateBefore(trackerId, from.get(), to.get());
        }
        if (from.isPresent()) {
            return trackerDataRepo.findAllByTrackerIdAndDateAfter(trackerId, from.get());
        }
        if (to.isPresent()) {
            return trackerDataRepo.findAllByTrackerIdAndDateBefore(trackerId, to.get());
        }
        return trackerDataRepo.findAllByTrackerId(trackerId);
    }
}
