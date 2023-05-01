package com.zpsm.rpgsessionassisstant.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class NotGamemasterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2708435581968759049L;

}
