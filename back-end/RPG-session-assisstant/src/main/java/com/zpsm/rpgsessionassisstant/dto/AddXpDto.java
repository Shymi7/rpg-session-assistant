package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AddXpDto(@NotNull Long characterId,
                       @NotNull Long roomId,
                       @NotNull @Min(1) @Max(100) Integer experience) implements Serializable {
}
