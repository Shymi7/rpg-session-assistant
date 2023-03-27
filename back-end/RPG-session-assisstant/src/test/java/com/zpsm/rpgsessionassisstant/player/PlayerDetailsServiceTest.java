package com.zpsm.rpgsessionassisstant.player;

import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerDetailsServiceTest {

    @Mock
    private PlayerRepository mockPlayerRepository;
    private PlayerDetailsService playerDetailsService;

    @BeforeEach
    void setUp() {
        playerDetailsService = new PlayerDetailsService(mockPlayerRepository, new BCryptPasswordEncoder());
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

}
