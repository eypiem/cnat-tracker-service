package dev.apma.cnat.trackerservice.rest.controller;


import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tracker-data")
public class TrackerDataRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerDataRestController.class);

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @GetMapping("/get/{trackerId}")
    public List<TrackerData> getTrackerData(@PathVariable String trackerId,
                                            @RequestParam Optional<Instant> from,
                                            @RequestParam Optional<Instant> to) {
        LOGGER.info("/tracker-data/get/{} from: {} to: {}", trackerId, from, to);
        if (from.isPresent() && to.isPresent()) {
            return trackerDataRepo.findAllByTrackerIdAndDateAfterAndDateBefore(trackerId, from.get(), to.get());
        }
        if (from.isPresent()) {
            return trackerDataRepo.findAllByTrackerIdAndDateAfter(trackerId, from.get());
        }
        if (to.isPresent()) {
            return trackerDataRepo.findAllByTrackerIdAndDateBefore(trackerId, to.get());
        }
        return trackerDataRepo.findAllByTrackerId(trackerId);
    }
}
