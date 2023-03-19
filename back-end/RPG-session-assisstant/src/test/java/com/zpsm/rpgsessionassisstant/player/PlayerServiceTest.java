package com.zpsm.rpgsessionassisstant.player;

import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository mockPlayerRepository;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService(mockPlayerRepository, new BCryptPasswordEncoder());
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
        playerService.registerNewPlayer(player);
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
        assertThrows(LoginAlreadyTakenException.class, () -> playerService.registerNewPlayer(player));
    }

}
