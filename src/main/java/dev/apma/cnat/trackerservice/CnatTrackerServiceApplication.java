package dev.apma.cnat.trackerservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CnatTrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CnatTrackerServiceApplication.class, args);
    }
}
