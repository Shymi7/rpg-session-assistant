package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
