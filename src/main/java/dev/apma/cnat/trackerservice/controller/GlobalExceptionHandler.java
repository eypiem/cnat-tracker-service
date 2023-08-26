package dev.apma.cnat.trackerservice.controller;


import dev.apma.cnat.trackerservice.dto.ValidationErrorsDTO;
import dev.apma.cnat.trackerservice.exception.TrackerDoesNotExistException;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nullable MethodArgumentNotValidException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        return new ResponseEntity<>(ex == null ? null : ValidationErrorsDTO.fromFieldError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TrackerDoesNotExistException.class})
    protected ResponseEntity<Object> handleTrackerDoesNotExistException(TrackerDoesNotExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

