package com.flex.orders.exception;

import org.springframework.http.ResponseEntity;
// import com.flex.orders.exception.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    // Handles @Valid failure
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse(400, message);
        return ResponseEntity.badRequest().body(error);
    }

    // Generic handler for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Internal server error occurred");
        return ResponseEntity.status(500).body(error);
    }
}