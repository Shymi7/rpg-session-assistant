package com.zpsm.rpgsessionassisstant.room.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Character} entity
 */
public record CharacterDto(Long id, @Size(max = 30) @NotNull String name, @NotNull Integer level,
                           @NotNull Integer health, @NotNull Integer skillPoints, @NotNull Integer experience,
                           Set<Long> itemIds, Set<Long> questIds,
                           Set<Long> characterAttributeIds) implements Serializable {
}
