package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Character_Attribute", schema = "zpsm_projekt")
public class CharacterAttribute {
    @EmbeddedId
    private CharacterAttributeKey characterAttributeKey;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("characterId")
    @JoinColumn(name = "character_id")
    private Character character;

    @NotNull
    @Column(name = "attribute_level", nullable = false)
    private Integer attributeLevel;

}
