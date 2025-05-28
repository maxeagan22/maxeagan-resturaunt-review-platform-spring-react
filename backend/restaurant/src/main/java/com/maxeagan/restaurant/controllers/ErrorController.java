package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.dtos.ErrorDto;
import com.maxeagan.restaurant.exceptions.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
