package com.zpsm.rpgsessionassisstant.player;

import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewPlayer(Player player) {
        playerRepository.findByLogin(player.getLogin())
            .ifPresent(value -> {
                log.error("Login '{}' is already taken", player.getLogin());
                throw new LoginAlreadyTakenException(
                    String.format("Login '%s' is already taken", player.getLogin()));
            });
        String encodedPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(encodedPassword);
        playerRepository.save(player);
    }

}
