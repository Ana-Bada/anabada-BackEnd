package com.anabada.anabadaBackend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> notValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.badRequest().body(msg);
    }

    @ExceptionHandler
    public ResponseEntity<String> illegal(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
