package com.example.demo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles exceptions thrown in the application and returns appropriate HTTP responses.
 * It uses the {@code @RestControllerAdvice} annotation to intercept exceptions thrown from any controller.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles {@link MethodArgumentNotValidException} and {@link IllegalArgumentException} by returning a map of field names and error messages.
     * If the exception is a {@link MethodArgumentNotValidException}, it retrieves all errors from the binding result and adds them to the map.
     * If the exception is an {@link IllegalArgumentException}, it adds a single error with the field name set to "error" and the message from the exception.
     *
     * @param ex The exception to handle
     * @return A {@link ResponseEntity} containing a map of field names and error messages, with HTTP status code 400 (Bad Request)
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            validationEx.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else if (ex instanceof IllegalArgumentException) {
            errors.put("error", ex.getMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles {@link ResourceNotFoundException} by returning a map containing the error message with HTTP status code 404 (Not Found).
     *
     * @param ex The exception to handle
     * @return A {@link ResponseEntity} containing the error message, with HTTP status code 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorMessage());
    }

    /**
     * Handles {@link AccessDeniedException} by returning the exception message with HTTP status code 403 (Forbidden).
     *
     * @param ex The exception to handle
     * @return A {@link ResponseEntity} containing the exception message, with HTTP status code 403 (Forbidden)
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
