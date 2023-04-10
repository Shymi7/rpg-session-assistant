package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * A DTO for the {@link com.zpsm.rpgsessionassisstant.model.Room} entity
 */
public record CreateRoomDto(@NotNull @Min(1L) Integer capacity, @NotNull String password,
                            @Size(max = 30) @NotBlank String name) implements Serializable {
}
