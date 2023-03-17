package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Embeddable
public class ItemAttributeKey implements Serializable {

    @Serial
    private static final long serialVersionUID = -874955735612765938L;

    @NotNull
    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Long itemId;

}
