package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnterRoomDto(@NotBlank String roomName, @NotBlank String password, @NotNull Long characterId) {
}
