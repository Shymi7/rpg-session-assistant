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
@Table(name = "Room", schema = "zpsm_projekt")
public class Room {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "room_seq")
    @SequenceGenerator(
        name = "room_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "room_id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gamemaster_id", nullable = false)
    private Gamemaster gamemaster;

    @ManyToMany
    @JoinTable(name = "Room_Character",
        schema = "zpsm_projekt",
        joinColumns = @JoinColumn(name = "Room_id"),
        inverseJoinColumns = @JoinColumn(name = "Character_id"))
    private Set<Character> character = new LinkedHashSet<>();

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 30)
    @NotNull
    @Column(name = "name", nullable = false, length = 30)
    private String name;

}
