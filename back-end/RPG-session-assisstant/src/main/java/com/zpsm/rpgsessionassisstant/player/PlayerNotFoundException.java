package com.zpsm.rpgsessionassisstant.player;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

@StandardException
public class PlayerNotFoundException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 6907929501659210170L;

}
