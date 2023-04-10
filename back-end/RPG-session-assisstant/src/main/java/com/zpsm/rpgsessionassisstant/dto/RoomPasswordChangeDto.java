package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomPasswordChangeDto(@NotBlank Long roomId, @NotBlank String newPassword) {
}
