package com.zpsm.rpgsessionassisstant.util;

import java.util.Map;

public class ErrorsMapper {

    public static  <T> Map<String, T> getErrorsMap(T error) {
        return Map.of("errors", error);
    }

}
