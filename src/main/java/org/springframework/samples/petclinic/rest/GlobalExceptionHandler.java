package org.springframework.samples.petclinic.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

/**
 * Global exception handler for REST controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        ApiError apiError = new ApiError(
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST.toString(),
            "Validation error"
        );

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (String error : errors) {
            apiError.addDetail(error);
        }

        ApiResponse<Void> response = ApiResponse.error("Validation failed", apiError);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle entity not found exceptions
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {

        ApiError apiError = new ApiError(
            "ENTITY_NOT_FOUND",
            HttpStatus.NOT_FOUND.toString(),
            ex.getMessage()
        );

        ApiResponse<Void> response = ApiResponse.error("Entity not found", apiError);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        ApiError apiError = new ApiError(
            "ILLEGAL_ARGUMENT",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getMessage()
        );

        ApiResponse<Void> response = ApiResponse.error("Invalid argument", apiError);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(
            Exception ex, WebRequest request) {

        ApiError apiError = new ApiError(
            "INTERNAL_SERVER_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            "An unexpected error occurred"
        );

        apiError.addDetail(ex.getMessage());

        ApiResponse<Void> response = ApiResponse.error("Server error", apiError);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
