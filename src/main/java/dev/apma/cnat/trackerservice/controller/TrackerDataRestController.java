package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.dto.TrackerDataDTO;
import dev.apma.cnat.trackerservice.service.TrackerDataService;
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

/**
 * This class is a controller for REST API endpoints related to tracker data.
 *
 * @author Amir Parsa Mahdian
 */
@RestController
public class TrackerDataRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerDataRestController.class);

    private final TrackerDataService trackerDataSvc;

    @Autowired
    public TrackerDataRestController(TrackerDataService trackerDataSvc) {
        this.trackerDataSvc = trackerDataSvc;
    }

    @GetMapping("/{trackerId}/data")
    public List<TrackerDataDTO> getTrackerData(@PathVariable String trackerId,
                                               @RequestParam Optional<Instant> from,
                                               @RequestParam Optional<Instant> to,
                                               @RequestParam Optional<Boolean> hasCoordinates,
                                               @RequestParam Optional<Integer> limit) {
        LOGGER.info("get /{}/data from: {} to: {} hasCoordinates: {} limit: {}",
                trackerId,
                from,
                to,
                hasCoordinates,
                limit);
        return trackerDataSvc.getTrackerData(trackerId,
                from.orElse(null),
                to.orElse(null),
                hasCoordinates.orElse(null),
                limit.orElse(null));
    }

    @GetMapping("/data/latest")
    public List<TrackerDataDTO> getLatestTrackerData(@RequestParam String userId) {
        LOGGER.info("get /data/latest {}", userId);
        return trackerDataSvc.getLatestTrackerData(userId);
    }
}
