package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GamemasterService {

    private final GamemasterRepository gamemasterRepository;
    private final PlayerRepository playerRepository;

    public Gamemaster createGamemaster(Player player) {
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setPlayer(player);
        Gamemaster saved = gamemasterRepository.save(gamemaster);
        player.getGamemasters().add(saved);
        playerRepository.save(player);
        return saved;
    }

}
