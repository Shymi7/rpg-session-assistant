package com.zpsm.rpgsessionassisstant.room.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Gamemaster} entity
 */
public record GamemasterDto(Long id, Long playerId, Long roomId) implements Serializable {
}
