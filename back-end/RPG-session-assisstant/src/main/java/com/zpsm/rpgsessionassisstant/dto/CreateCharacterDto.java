package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCharacterDto(@NotBlank @Size(min = 3, max = 30) String name, @NotNull Long roomId,
                                 @Size(min = 1) List<String> attributeNames) {
}
