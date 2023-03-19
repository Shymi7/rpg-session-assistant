package com.zpsm.rpgsessionassisstant.player;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class LoginAlreadyTakenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7589470976065312290L;

}
