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
@Table(name = "Quest", schema = "zpsm_projekt")
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 250)
    @NotNull
    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @ManyToMany
    @JoinTable(name = "Quest_Character",
        schema = "zpsm_projekt",
        joinColumns = @JoinColumn(name = "Quest_id"),
        inverseJoinColumns = @JoinColumn(name = "Character_id"))
    private Set<Character> characters = new LinkedHashSet<>();

}
