package dev.apma.cnat.trackerservice.rest.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tracker")
public class TrackerRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerRestController.class);

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @Autowired
    private TrackerRepository trackerRepo;

    @PostMapping("/register")
    public Tracker registerNewTracker(@RequestBody Tracker tracker) {
        LOGGER.info("/tracker/register: {}", tracker);
        return trackerRepo.save(tracker);
    }

    @GetMapping("/get")
    public List<Tracker> getUserTrackers(@RequestParam String userId) {
        LOGGER.info("/tracker/get: {}", userId);
        return trackerRepo.findAllByUserId(userId);
    }

    @GetMapping("/get/{trackerId}")
    public Tracker getTracker(@PathVariable String trackerId) {
        LOGGER.info("/tracker/get/{}", trackerId);
        return trackerRepo.findById(trackerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracker does not exist"));
    }

    @DeleteMapping("/delete/{trackerId}")
    public void deleteUserTracker(@PathVariable String trackerId) {
        LOGGER.info("/tracker/delete/{}", trackerId);
        var tracker = trackerRepo.findById(trackerId);
        if (tracker.isPresent()) {
            trackerDataRepo.deleteAllByTrackerId(trackerId);
            trackerRepo.deleteById(trackerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracker does not exist");
        }
    }
}
