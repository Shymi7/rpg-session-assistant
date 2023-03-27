package com.zpsm.rpgsessionassisstant.registration;

import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.player.PlayerDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {

    private final PlayerDetailsService playerDetailsService;

    public void register(RegistrationRequest request) {
        Player player = mapRequestToPlayer(request);
        playerDetailsService.registerNewPlayer(player);
        log.info("New player registered");
    }

    private Player mapRequestToPlayer(RegistrationRequest request) {
        Player player = new Player();
        player.setLogin(request.login());
        player.setPassword(request.password());
        return player;
    }

}
