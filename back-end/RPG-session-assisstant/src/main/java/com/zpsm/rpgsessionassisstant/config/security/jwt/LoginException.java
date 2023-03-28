package com.zpsm.rpgsessionassisstant.config.security.jwt;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

@StandardException
public class LoginException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = -8340854371512350947L;

}
