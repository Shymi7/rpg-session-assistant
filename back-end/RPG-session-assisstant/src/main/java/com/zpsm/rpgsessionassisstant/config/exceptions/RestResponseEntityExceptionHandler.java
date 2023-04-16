package com.zpsm.rpgsessionassisstant.config.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zpsm.rpgsessionassisstant.config.security.jwt.MissingTokenException;
import com.zpsm.rpgsessionassisstant.exception.LoginAlreadyTakenException;
import com.zpsm.rpgsessionassisstant.exception.RoomException;
import com.zpsm.rpgsessionassisstant.util.ErrorsMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MissingTokenException.class,
        RoomException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleLoginTakenError(LoginAlreadyTakenException e) {
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({JWTVerificationException.class, AccessDeniedException.class})
    public ResponseEntity<Map<String, String>> handleJwtErrors(LoginAlreadyTakenException e) {
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(e.getMessage()), HttpStatus.FORBIDDEN);
    }

}
