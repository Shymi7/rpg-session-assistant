package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

public record CreateItemDto(@Size(min = 3,max = 40) @NotNull String name, @NotNull String description,
                      Set<CreateItemAttributeDto> itemAttributes) implements Serializable {
}
