package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.GamemasterDto;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import org.springframework.stereotype.Component;

@Component
public class GamemasterMapper {

    public GamemasterDto mapToDto(Gamemaster gamemaster) {
        return new GamemasterDto(
            gamemaster.getId(),
            gamemaster.getPlayer().getId(),
            gamemaster.getRoom().getId());
    }

}
