package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record CreateItemAttributeDto(@NotNull CreateNewAttributeDto attribute,
                                     @NotNull Integer attributeValue) implements Serializable {
}
