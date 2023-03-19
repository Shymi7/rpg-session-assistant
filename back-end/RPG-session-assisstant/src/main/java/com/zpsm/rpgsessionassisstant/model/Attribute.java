package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Attribute", schema = "zpsm_projekt")
public class Attribute {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "attribute_seq")
    @SequenceGenerator(
        name = "attribute_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "attribute_id", nullable = false)
    private Long id;

    @Size(max = 25)
    @NotNull
    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @NotNull
    @OneToMany(mappedBy = "attribute")
    private Set<ItemAttribute> itemAttributes = new LinkedHashSet<>();

}
