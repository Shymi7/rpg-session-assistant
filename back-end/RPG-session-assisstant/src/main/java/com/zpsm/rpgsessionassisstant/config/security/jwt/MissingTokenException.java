package com.zpsm.rpgsessionassisstant.config.security.jwt;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class MissingTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3799931143271125372L;

}
