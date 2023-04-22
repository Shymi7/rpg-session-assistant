package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.PlayerDto;
import com.zpsm.rpgsessionassisstant.exception.LoginAlreadyTakenException;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import com.zpsm.rpgsessionassisstant.util.PlayerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerDetailsServiceTest {

    @Mock
    private PlayerRepository mockPlayerRepository;
    @Mock
    private PlayerMapper mockPlayerMapper;
    private PlayerDetailsService playerDetailsService;

    @BeforeEach
    void setUp() {
        playerDetailsService = new PlayerDetailsService(mockPlayerRepository, mockPlayerMapper, new BCryptPasswordEncoder());
    }

    @Test
    void givenCorrectPayloadAndFreeLoginShouldRegisterUser() {
        // given
        Player player = new Player();
        player.setLogin("new_user");
        player.setPassword("password123");

        // when
        when(mockPlayerRepository.findByLogin(player.getLogin())).thenReturn(Optional.empty());

        // then
        playerDetailsService.registerNewPlayer(player);
    }

    @Test
    void givenExistingLoginShouldThrowLoginAlreadyTakenException() {
        // given
        Player player = new Player();
        player.setLogin("new_user");
        player.setPassword("password123");
        Player playerResponse = new Player();
        player.setId(1L);
        player.setLogin(player.getLogin());
        player.setPassword(player.getPassword());

        // when
        when(mockPlayerRepository.findByLogin(player.getLogin())).thenReturn(Optional.of(playerResponse));

        // then
        assertThrows(LoginAlreadyTakenException.class, () -> playerDetailsService.registerNewPlayer(player));
    }

    @Test
    void givenExistingUsernameShouldReturnUserDetails() {
        // given
        String username = "player";
        Player expected = new Player();
        expected.setId(1L);
        expected.setLogin(username);
        expected.setPassword("password1");
        when(mockPlayerRepository.findByLogin(username)).thenReturn(Optional.of(expected));

        // when
        UserDetails actual = playerDetailsService.loadUserByUsername(username);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingUsernameShouldThrowPlayerNotFoundException() {
        // given
        String username = "unreal";
        when(mockPlayerRepository.findByLogin(username)).thenReturn(Optional.empty());

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> playerDetailsService.loadUserByUsername(username));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNonExistingUsernameShouldThrowPlayerNotFoundException(String username) {
        // given
        when(mockPlayerRepository.findByLogin(username)).thenReturn(Optional.empty());

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> playerDetailsService.loadUserByUsername(username));
    }

    @Test
    void givenExistingIdShouldReturnPlayer() {
        // given
        Player player = new Player();
        player.setId(1L);
        player.setLogin("Testowy");
        PlayerDto expected = new PlayerDto(player.getId(), player.getLogin(), Set.of(), Set.of());
        when(mockPlayerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(mockPlayerMapper.mapToDto(any())).thenReturn(expected);

        // when
        PlayerDto actual = playerDetailsService.getPlayerById(player.getId());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdShouldThrowPlayerNotFoundException() {
        // given
        when(mockPlayerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> playerDetailsService.getPlayerById(1L));
    }

    @Test
    void givenExistingLoginShouldReturnPlayer() {
        // given
        Player player = new Player();
        player.setId(1L);
        player.setLogin("Testowy");
        PlayerDto expected = new PlayerDto(player.getId(), player.getLogin(), Set.of(), Set.of());
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(player));
        when(mockPlayerMapper.mapToDto(any())).thenReturn(expected);

        // when
        PlayerDto actual = playerDetailsService.getPlayerByLogin(player.getLogin());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingLoginShouldThrowPlayerNotFoundException() {
        // given
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> playerDetailsService.getPlayerByLogin("Testowy"));
    }

}
