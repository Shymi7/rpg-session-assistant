package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Embeddable
public class CharacterAttributeKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -5752046475800764932L;

    @NotNull
    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @NotNull
    @Column(name = "character_id", nullable = false)
    private Long characterId;

}
