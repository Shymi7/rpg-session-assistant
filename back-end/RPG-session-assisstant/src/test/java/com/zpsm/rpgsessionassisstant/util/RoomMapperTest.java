package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.RoomDto;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Room;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomMapperTest {

    private RoomMapper roomMapper = new RoomMapper();

    @Test
    void givenRoomEntityShouldMapToDTO() {
        // given
        Room room = getCharacter();
        RoomDto expected = getRoomDTO();

        // when
        RoomDto actual = roomMapper.mapToDto(room);

        // then
        assertEquals(expected, actual);
    }

    private Room getCharacter() {
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setId(1L);
        Character character = new Character();
        character.setId(1L);
        Room room = new Room();
        room.setId(1L);
        room.setName("Room");
        room.setPassword("password");
        room.setCapacity(4);
        room.setGamemaster(gamemaster);
        room.setCharacters(Set.of(character));
        return room;
    }

    private RoomDto getRoomDTO() {
        return new RoomDto(
            1L,
            1L,
            Set.of(1L),
            4,
            "Room");
    }

}
