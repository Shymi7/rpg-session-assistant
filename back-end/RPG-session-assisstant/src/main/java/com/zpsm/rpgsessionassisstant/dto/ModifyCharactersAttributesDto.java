package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ModifyCharactersAttributesDto(@NotNull Long attributeId, @NotNull @Min(1) Integer skillPoints) {

}
