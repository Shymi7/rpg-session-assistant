package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GamemasterServiceTest {

    @Mock
    private GamemasterRepository mockGamemasterRepository;
    @Mock
    private PlayerDetailsService mockDetailsService;
    private GamemasterService service;

    @BeforeEach
    void setUp() {
        service = new GamemasterService(mockGamemasterRepository, mockDetailsService);
    }

    @Test
    void givenPlayerEntityShouldCreateGamemaster() {
        // given
        Player player = new Player();
        player.setId(1L);
        Gamemaster savedGamemaster = new Gamemaster();
        savedGamemaster.setId(1L);
        savedGamemaster.setPlayer(player);
        when(mockGamemasterRepository.save(any())).thenReturn(savedGamemaster);

        // when
        Gamemaster actual = service.createGamemaster(player);

        // then
        assertEquals(savedGamemaster, actual);
    }

}
