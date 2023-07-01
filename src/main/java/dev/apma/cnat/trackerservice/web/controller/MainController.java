package dev.apma.cnat.trackerservice.web.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.web.dto.TrackerDataDto;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private TrackerRepository trackerRepo;

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @GetMapping(path = "/trackers/get")
    @ResponseBody
    public List<Tracker> getUserTrackers(@RequestParam @NotNull String userId) {
        LOGGER.info("getUserTrackers: %s".formatted(userId));
        return trackerRepo.findTrackersByUserId(userId);
    }

    @GetMapping(path = "/tracker-data/get")
    @ResponseBody
    public List<TrackerData> getTrackerData(@RequestParam @NotNull String trackerId,
                                            @RequestParam Optional<Instant> from,
                                            @RequestParam Optional<Instant> to) {
        LOGGER.info("getTrackerData: %s".formatted(trackerId));
        if (from.isEmpty() && to.isEmpty()) {
            return trackerDataRepo.findAll(trackerId);
        } else if (from.isPresent()) {
            return trackerDataRepo.findAllByDateAfter(trackerId, from.get());
        } else {
            return trackerDataRepo.findAllByDateAfter(trackerId, to.get());
        }
    }


    @PostMapping(path = "/trackers/register")
    public void registerNewTracker(@RequestBody @NotNull Tracker tracker) {
        LOGGER.info("registerNewTracker: %s".formatted(tracker));
        trackerRepo.save(tracker);
    }

    @PostMapping(path = "/tracker-data/register")
    public void registerNewTrackerData(@RequestBody @NotNull TrackerDataDto trackerData) {
        LOGGER.info("registerNewTrackerData: %s".formatted(trackerData));
        Optional<Tracker> t = trackerRepo.findById(trackerData.trackerId());
        if (t.isPresent()) {
            trackerDataRepo.save(new TrackerData(t.get(), trackerData.data(), trackerData.timestamp()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "trackerId not found.");
        }
    }
}
