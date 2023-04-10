package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Room} entity
 */
public record RoomDto(Long id, Long gamemasterId, Set<Long> characterIds, @NotNull Integer capacity,
                      @Size(max = 30) @NotNull String name) implements Serializable {
}
