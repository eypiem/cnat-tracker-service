package dev.apma.cnat.trackerservice.rest.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracker")
public class TrackerRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerRestController.class);

    @Autowired
    private TrackerRepository trackerRepo;

    @PostMapping("/register")
    public Tracker registerNewTracker(@RequestBody @NotNull Tracker tracker) {
        LOGGER.info("/trackers/register: {}", tracker);
        return trackerRepo.save(tracker);
    }

    @GetMapping("/get")
    public List<Tracker> getUserTrackers(@RequestParam @NotNull String userId) {
        LOGGER.info("/trackers/get: %s".formatted(userId));
        return trackerRepo.findAllByUserId(userId);
    }
}
