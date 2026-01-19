package com.ecommerce_backend.exception;

import com.ecommerce_backend.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<?> handlenotfound(ResourceNotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
}
@ExceptionHandler(DuplicateResourceException.class)
public ResponseEntity<?> duplicatefound(DuplicateResourceException  e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
}

@ExceptionHandler(Exception.class)
public ResponseEntity<?> handleException(Exception e) {
    e.printStackTrace();
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
}

    // ✅ Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        APIResponse<Map<String, String>> response = new APIResponse<>();
        response.setSuccess(false);
        response.setMessage("Validation failed");
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
