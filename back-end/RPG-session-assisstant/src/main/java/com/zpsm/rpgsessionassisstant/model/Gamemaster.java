package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Gamemaster", schema = "zpsm_projekt")
public class Gamemaster {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "gamemaster_seq")
    @SequenceGenerator(
        name = "gamemaster_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "gamemaster_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @NotNull
    @OneToOne(mappedBy = "gamemaster")
    private Room room;

}
