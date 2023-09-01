package dev.apma.cnat.trackerservice;


import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import dev.apma.cnat.trackerservice.service.TrackerDataService;
import dev.apma.cnat.trackerservice.service.TrackerDataServiceImpl;
import dev.apma.cnat.trackerservice.service.TrackerService;
import dev.apma.cnat.trackerservice.service.TrackerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@Configuration
@EnableMongoRepositories
public class MongoDBTestContainerConfig {
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(27017);

    static {
        mongoDBContainer.start();
        var mappedPort = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongodb.container.port", String.valueOf(mappedPort));
    }

    @Bean
    public TrackerService trackerService(TrackerRepository trackerRepository,
                                         TrackerDataRepository trackerDataRepository) {
        return new TrackerServiceImpl(trackerRepository, trackerDataRepository);
    }

    @Bean
    public TrackerDataService trackerDataService(TrackerRepository trackerRepository,
                                                 TrackerDataRepository trackerDataRepository) {
        return new TrackerDataServiceImpl(trackerRepository, trackerDataRepository);
    }
}
