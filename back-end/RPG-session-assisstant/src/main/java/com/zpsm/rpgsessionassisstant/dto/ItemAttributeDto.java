package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.ItemAttribute} entity
 */
public record ItemAttributeDto(@NotNull AttributeDto attribute,
                               @NotNull Integer attributeValue) implements Serializable {
}
