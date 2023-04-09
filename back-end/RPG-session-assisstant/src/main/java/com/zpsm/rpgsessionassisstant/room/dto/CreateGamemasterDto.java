package com.zpsm.rpgsessionassisstant.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Gamemaster} entity
 */
public record CreateGamemasterDto(@NotNull Long playerId, @NotBlank String player) implements Serializable {
}
