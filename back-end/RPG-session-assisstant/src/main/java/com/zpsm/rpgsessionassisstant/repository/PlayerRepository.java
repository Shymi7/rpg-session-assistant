package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByLogin(String login);

}
