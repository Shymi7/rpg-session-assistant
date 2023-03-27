package com.zpsm.rpgsessionassisstant.player;

import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerDetailsService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return playerRepository.findByLogin(username)
            .orElseThrow(() -> {
                log.error("Player with login {} not found", username);
                return new PlayerNotFoundException(String.format("Player with login %s not found", username));
            });
    }

}
