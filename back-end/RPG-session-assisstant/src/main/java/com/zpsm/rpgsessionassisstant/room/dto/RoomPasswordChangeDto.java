package com.zpsm.rpgsessionassisstant.room.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomPasswordChangeDto(@NotBlank Long roomId, @NotBlank String newPassword) {
}
