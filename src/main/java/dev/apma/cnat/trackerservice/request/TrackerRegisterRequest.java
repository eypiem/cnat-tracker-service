package dev.apma.cnat.trackerservice.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * This request class defines the body for a <i>tracker register</i> request.
 *
 * @author Amir Parsa Mahdian
 */
public record TrackerRegisterRequest(@Indexed @NotBlank(message = "User ID must not be blank") String userId,
                                     @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") String name) {
}
