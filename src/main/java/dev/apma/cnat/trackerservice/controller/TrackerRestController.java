package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.dto.TrackerDTO;
import dev.apma.cnat.trackerservice.exceptions.TrackerServiceException;
import dev.apma.cnat.trackerservice.requests.TrackerRegisterRequest;
import dev.apma.cnat.trackerservice.service.TrackerService;
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
    public TrackerDTO getTracker(@PathVariable String trackerId) {
        LOGGER.info("get /{}", trackerId);
        try {
            return trackerSvc.getTracker(trackerId);
        } catch (TrackerServiceException e) {
            LOGGER.error("Failed to get tracker with id [{}]: {}", trackerId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get tracker");
        }
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
