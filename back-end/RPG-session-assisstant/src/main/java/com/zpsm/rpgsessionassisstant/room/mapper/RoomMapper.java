package com.zpsm.rpgsessionassisstant.room.mapper;

import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Room;
import com.zpsm.rpgsessionassisstant.room.dto.RoomDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomDto mapToDto(Room room) {
        return new RoomDto(
            room.getId(),
            room.getGamemaster().getId(),
            room.getCharacter()
                .stream()
                .map(Character::getId)
                .collect(Collectors.toSet()),
            room.getCapacity(),
            room.getName());
    }

}
