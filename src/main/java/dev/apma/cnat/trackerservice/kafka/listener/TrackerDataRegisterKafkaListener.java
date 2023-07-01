package dev.apma.cnat.trackerservice.kafka.listener;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.web.dto.TrackerDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@KafkaListener(topics = "${app.kafka.topics.tracker-data-register}")
public class TrackerDataRegisterKafkaListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerDataRegisterKafkaListener.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TrackerRepository trackerRepo;

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    @KafkaHandler
    void listen(TrackerDataDto data) {

        LOGGER.info("TrackerDataRegisterListener {}", data);

        Optional<Tracker> t = trackerRepo.findById(data.trackerId());
        if (t.isPresent()) {
            trackerDataRepo.save(new TrackerData(t.get(), data.data(), data.timestamp()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "trackerId not found.");
        }
    }
}