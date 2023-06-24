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
@KafkaListener(topics = KafkaTopics.TRACKER_STATUS_UPDATE_REQ)
public class TrackerRegisterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerRegisterController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public TrackerRegisterController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
    void listen(String message) {
        System.out.println("TrackerRegisterController[String] {}" + message);
        LOGGER.info("TrackerRegisterController[String] {}", message);
        kafkaTemplate.send(KafkaTopics.TRACKER_STATUS_UPDATE, "New tracker registered.");
    }

    @KafkaHandler(isDefault = true)
    void listenDefault(Object object) {
        System.out.println("TrackerRegisterController[Default] {}");
        LOGGER.info("TrackerRegisterController[Default] {}", object);
    }
}
