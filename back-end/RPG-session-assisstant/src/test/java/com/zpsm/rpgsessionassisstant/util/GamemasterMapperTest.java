package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.GamemasterDto;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GamemasterMapperTest {

    private GamemasterMapper gamemasterMapper = new GamemasterMapper();

    @Test
    void givenGamemasterEntityShouldMapToDto() {
        // given
        Player player = new Player();
        player.setId(1L);
        Room room = new Room();
        room.setId(2L);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setId(3L);
        gamemaster.setPlayer(player);
        gamemaster.setRoom(room);
        GamemasterDto expected = new GamemasterDto(gamemaster.getId(), player.getId(), room.getId());

        // when
        GamemasterDto actual = gamemasterMapper.mapToDto(gamemaster);

        // then
        assertEquals(expected, actual);
    }

}
