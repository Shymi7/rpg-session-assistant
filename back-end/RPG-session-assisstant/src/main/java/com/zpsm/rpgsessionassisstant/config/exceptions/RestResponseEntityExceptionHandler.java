package com.zpsm.rpgsessionassisstant.config.exceptions;

import com.zpsm.rpgsessionassisstant.player.LoginAlreadyTakenException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleLoginTakenError(LoginAlreadyTakenException e) {
        return new ResponseEntity<>(getErrorsMap(e.getMessage()), HttpStatus.CONFLICT);
    }

    private <T> Map<String, T> getErrorsMap(T error) {
        return Map.of("errors", error);
    }

}
