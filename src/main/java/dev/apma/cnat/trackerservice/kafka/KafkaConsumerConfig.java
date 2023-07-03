package dev.apma.cnat.trackerservice.kafka;


import dev.apma.cnat.trackerservice.dto.TrackerDataDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServerUrl;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /// TODO: See if this can be specified in application.yml instead
    private Map<String, Object> baseKafkaProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerUrl);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return props;
    }

    @Bean
    public ConsumerFactory<String, TrackerDataDto> registerTrackerDataConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseKafkaProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(TrackerDataDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TrackerDataDto> registerTrackerDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TrackerDataDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(registerTrackerDataConsumerFactory());
        return factory;
    }
}