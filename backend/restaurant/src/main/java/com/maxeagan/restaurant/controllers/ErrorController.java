package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.dtos.ErrorDto;
import com.maxeagan.restaurant.exceptions.RestaurantNotFoundException;
import com.maxeagan.restaurant.exceptions.ReviewNotAllowedException;
import com.maxeagan.restaurant.exceptions.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global error handler for REST controllers.
 * Handles and logs exceptions and returns consistent error responses.
 *
 * Covers:
 * - StorageException (custom app-level exception)
 * - Generic fallback for all other uncaught exceptions
 */
@RestController
@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ErrorDto> handleReviewNotAllowedException(ReviewNotAllowedException ex){
        log.error("Caught ReviewNotAllowedException");

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("The specified review could not be created or updated. ")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        
    }

    /**
     * Exception handler for {@link RestaurantNotFoundException}.
     *
     * <p>This method is invoked when a {@code RestaurantNotFoundException} is thrown anywhere
     * in the controller layer. It logs the exception and returns a standardized error response
     * with HTTP 404 (Not Found) status.</p>
     *
     * @param ex the {@code RestaurantNotFoundException} that was thrown
     * @return a {@code ResponseEntity} containing an {@code ErrorDto} with a 404 status and
     *         a descriptive error message
     */
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorDto> handleRestaurantNotFoundException(RestaurantNotFoundException ex){
        log.error("Caught RestaurantNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("The specified restaurant was not found")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation errors thrown when method arguments annotated with {@code @Valid} fail validation.
     * <p>
     * Specifically handles {@link MethodArgumentNotValidException}, which is typically thrown when
     * request body validation fails (e.g., invalid input in DTO fields).
     * <p>
     * Aggregates all field validation errors into a single error message string and returns it in a
     * structured {@link ErrorDto} with HTTP 400 (Bad Request) status.
     *
     * @param ex the exception containing details of validation failures
     * @return ResponseEntity containing a structured error message and 400 status code
     */
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException");

        String errorMessage = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles storage-related exceptions (e.g., file I/O, disk issues).
     *
     * @param ex the caught StorageException
     * @return standardized 500 error response
     */
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDto> handleStorageException(StorageException ex){
        log.error("Caught StorageException", ex);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unable to save or retrieve resources at this time")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generic catch-all for unhandled exceptions.
     *
     * @param ex the caught exception
     * @return standardized 500 error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught unexpected exception", ex);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
