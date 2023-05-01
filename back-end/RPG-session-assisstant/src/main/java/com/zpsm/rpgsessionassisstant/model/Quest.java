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
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "quest_seq")
    @SequenceGenerator(
        name = "quest_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "quest_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Size(max = 250)
    @NotNull
    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @ManyToMany(mappedBy = "quests")
    private Set<Character> characters = new LinkedHashSet<>();

}
