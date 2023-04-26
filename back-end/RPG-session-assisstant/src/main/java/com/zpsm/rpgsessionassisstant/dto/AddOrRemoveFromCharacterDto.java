package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;

public record AddOrRemoveFromCharacterDto(@NotNull Long characterId, @NotNull Long entityId) {
}
