package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Query("select ch from Character ch join ch.player where ch.player.id = :playerId")
    Collection<Character> findAllPlayersCharacters(Long playerId);

}
