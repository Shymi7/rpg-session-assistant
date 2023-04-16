package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Item_Attribute", schema = "zpsm_projekt")
public class ItemAttribute {

    @EmbeddedId
    private ItemAttributeKey id = new ItemAttributeKey();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    @NotNull
    @Column(name = "attribute_value", nullable = false)
    private Integer attributeValue;

}
