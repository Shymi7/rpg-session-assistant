package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Player} entity
 */
public record PlayerDto(Long id, @Size(max = 30) @NotNull String login, Set<GamemasterDto> gamemasters,
                        Set<CharacterDto> characters) implements Serializable {
}
