package com.zpsm.rpgsessionassisstant.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNewAttributeDto(@Size(min = 3, max = 25) @NotBlank String name) {
}
