package com.zpsm.rpgsessionassisstant.room.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomNameChangeDto(@NotBlank Long roomId, @NotBlank String newRoomName) {
}
