package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.CharacterAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CharacterAttributeRepository extends JpaRepository<CharacterAttribute, CharacterAttributeKey> {

    @Modifying
    @Query("update CharacterAttribute set attributeLevel = :newAttributeLevel " +
        "where characterAttributeKey.characterId = :characterId and characterAttributeKey.attributeId = :attributeId")
    int updateAttributeLevel(long attributeId, long characterId, int newAttributeLevel);

}
