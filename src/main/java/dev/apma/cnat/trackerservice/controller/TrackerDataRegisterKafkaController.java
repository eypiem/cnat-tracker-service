package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.requests.TrackerDataRegisterRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrackerDataRegisterKafkaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerDataRegisterKafkaController.class);

    private final TrackerRepository trackerRepo;

    private final TrackerDataRepository trackerDataRepo;

    @Autowired
    public TrackerDataRegisterKafkaController(TrackerRepository trackerRepo, TrackerDataRepository trackerDataRepo) {
        this.trackerRepo = trackerRepo;
        this.trackerDataRepo = trackerDataRepo;
    }

    @KafkaListener(topics = "${app.kafka.topics.tracker-data-register}", properties = {
            "spring.json.value.default.type=dev.apma.cnat.trackerservice.dto.TrackerDataDto"})
    void listen(@Valid @Payload TrackerDataRegisterRequest tdrr) {
        LOGGER.info("TrackerDataRegisterListener {}", tdrr);

        Optional<Tracker> t = trackerRepo.findById(tdrr.tracker().id());
        if (t.isPresent()) {
            trackerDataRepo.save(new TrackerData(t.get(), tdrr.data(), tdrr.timestamp()));
            LOGGER.info("Tracker data saved.");
        } else {
            LOGGER.error("trackerId not found.");
        }
    }
}
