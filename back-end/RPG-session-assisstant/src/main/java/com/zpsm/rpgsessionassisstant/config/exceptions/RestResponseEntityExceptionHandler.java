package com.zpsm.rpgsessionassisstant.config.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zpsm.rpgsessionassisstant.config.security.jwt.MissingTokenException;
import com.zpsm.rpgsessionassisstant.exception.*;
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
        FullRoomException.class,
    })
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlenotFoundErrors(EntityNotFoundException e) {
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleLoginTakenError(LoginAlreadyTakenException e) {
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
        JWTVerificationException.class,
        AccessDeniedException.class,
        NotGamemasterException.class,
        CharacterNotInAnyRoomException.class
    })
    public ResponseEntity<Map<String, String>> handleJwtErrors(RuntimeException e) {
        return new ResponseEntity<>(ErrorsMapper.getErrorsMap(e.getMessage()), HttpStatus.FORBIDDEN);
    }

}
