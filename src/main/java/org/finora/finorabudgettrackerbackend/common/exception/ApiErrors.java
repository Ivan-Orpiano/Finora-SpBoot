package org.finora.finorabudgettrackerbackend.common.exception;


import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.util.Map;

//Handles errors from all REST controllers in one place
@RestControllerAdvice
public class ApiErrors {

    //handles invalid financial amounts and other business inputs
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(
            IllegalArgumentException exception ) {

        return ResponseEntity.badRequest().body(
                Map.of("message", exception.getMessage())
        );
    }

    //handles incorrect and or ivalid date formats
    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Map<String, String>> invalidDate(
            DateTimeException exception
    ) {

        return ResponseEntity.badRequest().body(
                Map.of("message", "Invalid date or month")
        );
    }

    // Converts @Valid field errors into readable responses
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationError(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Invalid request");

        return ResponseEntity.badRequest().body(Map.of("message", message));
    }

    //handles malformed JSON and invalid enum values
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> invalidJson(
            HttpMessageNotReadableException exception
    ) {
        return ResponseEntity.badRequest().body(
          Map.of("message", "Invalid JSON or Field value")
        );
    }
}
