package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnterRoomDto(@NotBlank Long roomId, @NotBlank String password, @NotNull Long characterId) {
}
