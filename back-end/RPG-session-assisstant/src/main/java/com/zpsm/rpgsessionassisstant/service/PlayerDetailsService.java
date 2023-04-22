package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.PlayerDto;
import com.zpsm.rpgsessionassisstant.exception.LoginAlreadyTakenException;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import com.zpsm.rpgsessionassisstant.util.PlayerMapper;
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
    private final PlayerMapper playerMapper;
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

    public PlayerDto getPlayerById(Long id) {
        return playerRepository.findById(id)
            .map(playerMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Player with id {} not found", id);
                return new PlayerNotFoundException(String.format("Player with id %d not found", id));
            });
    }

    public PlayerDto getPlayerByLogin(String login) {
        Player player = (Player) loadUserByUsername(login);
        return playerMapper.mapToDto(player);
    }

}
