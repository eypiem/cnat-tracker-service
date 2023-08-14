package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TrackerRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerRestController.class);

    private final TrackerDataRepository trackerDataRepo;

    private final TrackerRepository trackerRepo;

    @Autowired
    public TrackerRestController(TrackerDataRepository trackerDataRepo, TrackerRepository trackerRepo) {
        this.trackerDataRepo = trackerDataRepo;
        this.trackerRepo = trackerRepo;
    }

    @PostMapping("")
    public Tracker registerNewTracker(@Valid @RequestBody Tracker tracker) {
        LOGGER.info("post: {}", tracker);
        return trackerRepo.save(tracker);
    }

    @GetMapping("")
    public List<Tracker> getUserTrackers(@RequestParam String userId) {
        LOGGER.info("get: {}", userId);
        return trackerRepo.findAllByUserId(userId);
    }

    @GetMapping("/{trackerId}")
    public Tracker getTracker(@PathVariable String trackerId) {
        LOGGER.info("get /{}", trackerId);
        return trackerRepo.findById(trackerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracker does not exist"));
    }

    @DeleteMapping("/{trackerId}")
    public void deleteUserTracker(@PathVariable String trackerId) {
        LOGGER.info("delete /{}", trackerId);
        var tracker = trackerRepo.findById(trackerId);
        if (tracker.isPresent()) {
            trackerDataRepo.deleteAllByTrackerId(trackerId);
            trackerRepo.deleteById(trackerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracker does not exist");
        }
    }
}
