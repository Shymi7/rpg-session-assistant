package com.zpsm.rpgsessionassisstant.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7741571217242075665L;

}
