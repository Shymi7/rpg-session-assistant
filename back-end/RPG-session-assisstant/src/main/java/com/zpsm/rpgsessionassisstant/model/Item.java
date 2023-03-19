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
@Table(name = "Item", schema = "zpsm_projekt")
public class Item {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "item_seq")
    @SequenceGenerator(
        name = "item_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @Size(max = 40)
    @NotNull
    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "Item_Character",
        schema = "zpsm_projekt",
        joinColumns = @JoinColumn(name = "Item_id"),
        inverseJoinColumns = @JoinColumn(name = "Character_id"))
    private Set<Character> characters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "item")
    Set<ItemAttribute> itemAttributes = new LinkedHashSet<>();

}
