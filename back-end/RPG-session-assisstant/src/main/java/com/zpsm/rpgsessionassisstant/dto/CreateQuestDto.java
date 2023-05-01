package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record CreateQuestDto(@Size(min = 4, max = 50) @NotNull String name,
                             @Size(min = 5, max = 250) @NotNull String description) implements Serializable {
}
