package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerRegisterRequest;
import dev.apma.cnat.trackerservice.service.TrackerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is a controller for REST API endpoints related to trackers.
 *
 * @author Amir Parsa Mahdian
 */
@RestController
public class TrackerRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerRestController.class);

    private final TrackerService trackerSvc;

    @Autowired
    public TrackerRestController(TrackerService trackerSvc) {
        this.trackerSvc = trackerSvc;
    }

    @PostMapping("")
    public TrackerDTO registerTracker(@Valid @RequestBody TrackerRegisterRequest req) {
        LOGGER.info("post {}", req);
        return trackerSvc.registerTracker(req);
    }

    @GetMapping("")
    public List<TrackerDTO> getUserTrackers(@RequestParam String userId) {
        LOGGER.info("get {}", userId);
        return trackerSvc.getUserTrackers(userId);
    }

    @GetMapping("/{trackerId}")
    public TrackerDTO getTracker(@PathVariable String trackerId) throws TrackerDoesNotExistException {
        LOGGER.info("get /{}", trackerId);
        return trackerSvc.getTracker(trackerId);
    }

    @DeleteMapping("/{trackerId}")
    public void deleteTracker(@PathVariable String trackerId) {
        LOGGER.info("delete /{}", trackerId);
        trackerSvc.deleteTracker(trackerId);
    }

    @DeleteMapping("")
    public void deleteAllUserTrackers(@RequestParam String userId) {
        LOGGER.info("delete {}", userId);
        trackerSvc.deleteAllUserTrackers(userId);
    }
}
