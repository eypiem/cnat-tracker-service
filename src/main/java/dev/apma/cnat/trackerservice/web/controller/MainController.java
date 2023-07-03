package dev.apma.cnat.trackerservice.web.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        LOGGER.info("/trackers/get: %s".formatted(userId));
        return trackerRepo.findTrackersByUserId(userId);
    }

    @PostMapping(path = "/trackers/register")
    public Tracker registerNewTracker(@RequestBody @NotNull Tracker tracker) {
        LOGGER.info("/trackers/register: {}", tracker);
        return trackerRepo.save(tracker);
    }

    @GetMapping(path = "/tracker-data/get")
    @ResponseBody
    public List<TrackerData> getTrackerData(@RequestParam @NotNull String trackerId,
                                            @RequestParam Optional<Instant> from,
                                            @RequestParam Optional<Instant> to) {
        LOGGER.info("/tracker-data/get: %s".formatted(trackerId));
        if (from.isPresent() && to.isPresent()) {
            return trackerDataRepo.findAllByDateAfterAndDateBefore(trackerId, from.get(), to.get());
        }
        if (from.isPresent()) {
            return trackerDataRepo.findAllByDateAfter(trackerId, from.get());
        }
        if (to.isPresent()) {
            return trackerDataRepo.findAllByDateBefore(trackerId, to.get());
        }
        return trackerDataRepo.findAll(trackerId);
    }
}
