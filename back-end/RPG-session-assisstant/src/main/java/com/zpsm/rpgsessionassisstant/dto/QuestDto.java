package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Quest} entity
 */
public record QuestDto(Long id, @Size(min = 4, max = 50) @NotNull String name,
                       @Size(min = 5, max = 250) @NotNull String description) implements Serializable {
}
