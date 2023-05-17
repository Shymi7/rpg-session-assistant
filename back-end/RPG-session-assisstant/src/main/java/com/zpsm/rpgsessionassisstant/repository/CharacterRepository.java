package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Query("select ch from Character ch join ch.player where ch.player.id = :playerId")
    Collection<Character> findAllPlayersCharacters(Long playerId);

    @Query("select ch from Character ch join CharacterAttribute a " +
        "where ch.id = :characterId and a.attribute.id = :attributeId")
    Optional<Character> doesCharacterHasGivenAttributes(Long characterId, Long attributeId);

}
