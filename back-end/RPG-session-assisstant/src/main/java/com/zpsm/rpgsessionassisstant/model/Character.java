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
@Table(name = "Character", schema = "zpsm_projekt")
public class Character {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "character_seq")
    @SequenceGenerator(
        name = "character_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "character_id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @NotNull
    @Column(name = "health", nullable = false)
    private Integer health;

    @NotNull
    @Column(name = "skill_points", nullable = false)
    private Integer skillPoints;

    @NotNull
    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "Item_Character",
        schema = "zpsm_projekt",
        joinColumns = @JoinColumn(name = "Character_id"),
        inverseJoinColumns = @JoinColumn(name = "Item_id"))
    private Set<Item> items = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "Quest_Character",
        schema = "zpsm_projekt",
        joinColumns = @JoinColumn(name = "Character_id"),
        inverseJoinColumns = @JoinColumn(name = "Quest_id"))
    private Set<Quest> quests = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "characters")
    private Set<Room> rooms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "character")
    private Set<CharacterAttribute> characterAttributes = new LinkedHashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "char_player_id", nullable = false)
    private Player player;

    public void addItem(Item item) {
        this.items.add(item);
        item.getCharacters().add(this);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
        item.getCharacters().remove(this);
    }

    public void addQuest(Quest quest) {
        this.quests.add(quest);
        quest.getCharacters().add(this);
    }

    public void removeQuest(Quest quest) {
        this.quests.remove(quest);
        quest.getCharacters().remove(this);
    }

}
