package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
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
    public List<TrackerDataDTO> getLatestTrackerData(@RequestParam String userId) {
        LOGGER.info("get /latest {}", userId);
        return trackerRepo.findLatestTrackerDataWithCoordinatesByUserId(userId)
                .stream()
                .map(TrackerDataDTO::fromTrackerData)
                .toList();
    }

    @GetMapping("/{trackerId}/data")
    public List<TrackerDataDTO> getTrackerData(@PathVariable String trackerId,
                                               @RequestParam Optional<Instant> from,
                                               @RequestParam Optional<Instant> to,
                                               @RequestParam Optional<Boolean> hasLocation) {
        LOGGER.info("get /{}/data from: {} to: {} hasLocation: {}", trackerId, from, to, hasLocation);
        List<TrackerData> r;
        if (hasLocation.orElse(false)) {
            r = trackerDataRepo.findAllByTrackerIdWithCoordinates(trackerId);
        } else if (from.isPresent() && to.isPresent()) {
            r = trackerDataRepo.findAllByTrackerIdAndDateAfterAndDateBefore(trackerId, from.get(), to.get());
        } else if (from.isPresent()) {
            r = trackerDataRepo.findAllByTrackerIdAndDateAfter(trackerId, from.get());
        } else if (to.isPresent()) {
            r = trackerDataRepo.findAllByTrackerIdAndDateBefore(trackerId, to.get());
        } else {
            r = trackerDataRepo.findAllByTrackerId(trackerId);
        }
        return r.stream().map(TrackerDataDTO::fromTrackerData).toList();
    }
}
