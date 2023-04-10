package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Attribute} entity
 */
public record AttributeDto(Long id, @Size(max = 25) @NotBlank String name) implements Serializable {
}
