package com.maxeagan.restaurant.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard error response DTO used across the API for error handling.
 *
 * Fields:
 * - status: HTTP status code (e.g., 400, 500)
 * - message: human-readable error message
 *
 * Used by exception handlers to return consistent error responses to clients.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private Integer status;
    private String message;
}
