package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Item} entity
 */
public record ItemDto(Long id, @Size(max = 40) @NotNull String name, @NotNull String description,
                      Set<ItemAttributeDto> itemAttributes) implements Serializable {
}
