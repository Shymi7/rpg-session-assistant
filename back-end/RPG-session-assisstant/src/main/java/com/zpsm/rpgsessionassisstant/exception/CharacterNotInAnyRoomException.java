package com.zpsm.rpgsessionassisstant.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class CharacterNotInAnyRoomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2431316514651616746L;

}
