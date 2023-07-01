package dev.apma.cnat.trackerservice;


import dev.apma.cnat.trackerservice.model.Tracker;
import dev.apma.cnat.trackerservice.model.TrackerData;
import dev.apma.cnat.trackerservice.repository.TrackerDataRepository;
import dev.apma.cnat.trackerservice.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;
import java.util.Map;

@SpringBootApplication
@EnableMongoRepositories
public class CnatTrackerServiceApplication implements CommandLineRunner {

    @Autowired
    private TrackerRepository trackerRepo;

    @Autowired
    private TrackerDataRepository trackerDataRepo;

    public static void main(String[] args) {
        SpringApplication.run(CnatTrackerServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //        trackerRepo.save(new Tracker("8", "8976"));
        //        System.out.println("saved tracker");

        Tracker t = trackerRepo.findById("8").orElseThrow();
        //        System.out.println(t);

        trackerDataRepo.save(new TrackerData(t, Map.of("UV", 5), Instant.now()));
        System.out.println("saved tracker data");

        System.out.println(trackerDataRepo.findAll(t.id()));
    }
}
