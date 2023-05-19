package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AddXpDto(@NotNull Long characterId, @NotNull Long roomId, @NotNull Integer experience) implements Serializable {
}
