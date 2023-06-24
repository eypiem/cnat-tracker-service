package dev.apma.cnat.trackerservice.KafkaListener;

import dev.apma.cnat.trackerservice.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = KafkaTopics.TRACKER_DATA_UPDATE_REQ)
public class TrackerUpdateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerUpdateController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public TrackerUpdateController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
    void listen(String message) {
        System.out.println("TrackerUpdateController[String] {}" + message);
        LOGGER.info("TrackerUpdateController[String] {}", message);
        kafkaTemplate.send(KafkaTopics.TRACKER_DATA_UPDATE, "here's the new valid data from tracker.");
    }

    @KafkaHandler(isDefault = true)
    void listenDefault(Object object) {
        System.out.println("TrackerUpdateController[Default] {}");
        LOGGER.info("TrackerUpdateController[Default] {}", object);
    }
}
