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
@Table(name = "Player", schema = "zpsm_projekt")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "login", nullable = false, length = 30)
    private String login;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "player")
    private Set<Gamemaster> gamemasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Character> characters = new LinkedHashSet<>();

}
