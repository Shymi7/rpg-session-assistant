package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
