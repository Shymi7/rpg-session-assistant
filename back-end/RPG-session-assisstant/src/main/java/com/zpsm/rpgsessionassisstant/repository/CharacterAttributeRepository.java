package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.CharacterAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterAttributeRepository extends JpaRepository<CharacterAttribute, CharacterAttributeKey> {
}
