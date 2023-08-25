package dev.apma.cnat.trackerservice.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.index.Indexed;

public record TrackerRegisterRequest(@Indexed @NotBlank(message = "User ID must not be blank") String userId,
                                     @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") String name) {
}
