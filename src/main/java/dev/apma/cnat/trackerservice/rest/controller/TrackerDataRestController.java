package dev.apma.cnat.trackerservice.rest.controller;


import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.rest.request.GetTrackerDataRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracker-data")
public class TrackerDataRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrackerDataRestController.class);

    @Autowired
    private TrackerRepository trackerRepo;

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @PostMapping("/get")
    public List<TrackerData> getTrackerData(@RequestBody GetTrackerDataRequest body) {
        LOGGER.info("/tracker-data/get: {}", body);
        if (trackerRepo.findByIdAndUserId(body.tracker().id(), body.tracker().userId()) == null) {
            LOGGER.warn("Non matching user and tracker request detected.");
            return List.of();
        }
        if (body.from().isPresent() && body.to().isPresent()) {
            return trackerDataRepo.findAllByDateAfterAndDateBefore(body.tracker().id(),
                    body.from().get(),
                    body.to().get());
        }
        if (body.from().isPresent()) {
            return trackerDataRepo.findAllByDateAfter(body.tracker().id(), body.from().get());
        }
        if (body.to().isPresent()) {
            return trackerDataRepo.findAllByDateBefore(body.tracker().id(), body.to().get());
        }
        return trackerDataRepo.findAll(body.tracker().id());
    }
}
