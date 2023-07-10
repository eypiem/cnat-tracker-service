package dev.apma.cnat.trackerservice.rest.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tracker")
public class TrackerRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerRestController.class);

    @Autowired
    private TrackerRepository trackerRepo;

    @PostMapping("/register")
    public Tracker registerNewTracker(@RequestBody Tracker tracker) {
        LOGGER.info("/tracker/register: {}", tracker);
        return trackerRepo.save(tracker);
    }

    @GetMapping("/get/{id}")
    public Optional<Tracker> getUserTracker(@PathVariable String id) {
        LOGGER.info("/tracker/get/{}", id);
        return trackerRepo.findById(id);
    }

    @GetMapping("/get")
    public List<Tracker> getUserTrackers(@RequestParam @NotNull String userId) {
        LOGGER.info("/tracker/get: {}", userId);
        return trackerRepo.findAllByUserId(userId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserTracker(@PathVariable String id) {
        LOGGER.info("/tracker/delete/{}", id);
        trackerRepo.deleteById(id);
    }
}
