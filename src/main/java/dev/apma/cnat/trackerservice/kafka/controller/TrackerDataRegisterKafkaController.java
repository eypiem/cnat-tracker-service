package dev.apma.cnat.trackerservice.kafka.controller;


import dev.apma.cnat.trackerservice.dto.TrackerDataDto;
import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
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

    @Autowired
    private TrackerRepository trackerRepo;

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @KafkaListener(topics = "${app.kafka.topics.tracker-data-register}", containerFactory =
            "registerTrackerDataKafkaListenerContainerFactory")
    void listen(@Payload TrackerDataDto data) {
        LOGGER.info("TrackerDataRegisterListener {}", data);

        Optional<Tracker> t = trackerRepo.findById(data.trackerId());
        if (t.isPresent()) {
            trackerDataRepo.save(new TrackerData(t.get(), data.data(), data.timestamp()));
            LOGGER.info("Tracker data saved.");
        } else {
            LOGGER.error("trackerId not found.");
        }
    }
}
