package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.RoomDto;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Room;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomDto mapToDto(Room room) {
        return new RoomDto(
            room.getId(),
            room.getGamemaster().getId(),
            room.getCharacters()
                .stream()
                .map(Character::getId)
                .collect(Collectors.toSet()),
            room.getCapacity(),
            room.getName());
    }

}
