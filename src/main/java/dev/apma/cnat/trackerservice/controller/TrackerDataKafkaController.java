package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import dev.apma.cnat.trackerservice.request.TrackerDataRegisterRequest;
import dev.apma.cnat.trackerservice.service.TrackerDataService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class TrackerDataKafkaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerDataKafkaController.class);

    private final TrackerDataService trackerDataSvc;

    @Autowired
    public TrackerDataKafkaController(TrackerDataService trackerDataSvc) {
        this.trackerDataSvc = trackerDataSvc;
    }

    @KafkaListener(topics = "${app.kafka.topics.tracker-data-register}", properties = {
            "spring.json.value.default.type=dev.apma.cnat.trackerservice.dto.TrackerDataDTO"})
    void listen(@Valid @Payload TrackerDataRegisterRequest req) {
        LOGGER.info("TrackerDataRegisterListener {}", req);

        try {
            trackerDataSvc.register(req);
        } catch (TrackerDoesNotExistException e) {
            LOGGER.error("Failed to register tracker data {}: {}", req, e.getMessage());
        }
    }
}
