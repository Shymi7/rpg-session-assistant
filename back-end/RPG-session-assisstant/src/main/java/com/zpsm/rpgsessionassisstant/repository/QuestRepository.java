package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
