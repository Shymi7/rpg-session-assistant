package com.zpsm.rpgsessionassisstant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ModifyCharactersAttributesDto(@NotNull @NotEmpty Set<@Valid AttributeLevelPair> attributesLevelSet) {

    public record AttributeLevelPair(@NotNull Long attributeId, @NotNull @Min(1) Integer newLevel) {}

}
