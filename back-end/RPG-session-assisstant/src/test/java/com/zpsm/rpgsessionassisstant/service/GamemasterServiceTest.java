package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Room;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void givenCorrectRoomIdPlayerNameAndExistingGamemasterShouldPerformAction() {
        // given
        Room room = new Room();
        room.setId(1L);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setRoom(room);
        Player player = new Player();
        player.getGamemasters().add(gamemaster);
        Consumer<Gamemaster> consumer = gamemaster1 -> System.out.println("Action performed");

        when(mockDetailsService.loadUserByUsername(anyString())).thenReturn(player);

        // when // then
        service.findGamemasterForGivenRoomAndDoSomething(room.getId(), "Test", consumer);
    }

    @Test
    void givenRoomThatDoesntBelongToGivenGamemasterShouldThrowEntityNotFoundException() {
        // given
        Room room = new Room();
        room.setId(1L);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setRoom(room);
        Player player = new Player();
        player.getGamemasters().add(gamemaster);
        Consumer<Gamemaster> consumer = gamemaster1 -> System.out.println("Action performed");

        when(mockDetailsService.loadUserByUsername(anyString())).thenReturn(player);

        // when // then
        assertThrows(
            EntityNotFoundException.class,
            () -> service.findGamemasterForGivenRoomAndDoSomething(3, "Test", consumer));
    }

    @Test
    void givenPlayerWhoIsNotGamemasterShouldThrowEntityNotFoundException() {
        // given
        Room room = new Room();
        room.setId(1L);
        Player player = new Player();
        Consumer<Gamemaster> consumer = gamemaster1 -> System.out.println("Action performed");

        when(mockDetailsService.loadUserByUsername(anyString())).thenReturn(player);

        // when // then
        assertThrows(
            EntityNotFoundException.class,
            () -> service.findGamemasterForGivenRoomAndDoSomething(3, "Test", consumer));
    }



}
