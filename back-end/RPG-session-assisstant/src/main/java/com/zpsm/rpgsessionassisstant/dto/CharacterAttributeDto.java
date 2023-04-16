package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.CharacterAttribute} entity
 */
public record CharacterAttributeDto(@NotNull AttributeDto attribute,
                                    @NotNull Integer attributeLevel) implements Serializable {
}
