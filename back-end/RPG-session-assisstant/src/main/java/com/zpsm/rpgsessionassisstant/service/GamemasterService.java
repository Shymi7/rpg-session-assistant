package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@AllArgsConstructor
public class GamemasterService {

    private final GamemasterRepository gamemasterRepository;
    private final PlayerDetailsService playerDetailsService;

    public Gamemaster createGamemaster(Player player) {
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setPlayer(player);
        Gamemaster saved = gamemasterRepository.save(gamemaster);
        player.getGamemasters().add(saved);
        playerDetailsService.save(player);
        return saved;
    }

    public void findGamemasterForGivenRoomAndDoSomething(long roomId, String playerName, Consumer<Gamemaster> action) {
        Player player = (Player) playerDetailsService.loadUserByUsername(playerName);
        player.getGamemasters()
            .stream()
            .filter(gamemaster -> gamemaster.getRoom().getId() == roomId)
            .findFirst()
            .ifPresentOrElse(action, () -> {
                log.error("Gamemaster not found");
                throw new EntityNotFoundException("Gamemaster not found");
            });
    }

}
