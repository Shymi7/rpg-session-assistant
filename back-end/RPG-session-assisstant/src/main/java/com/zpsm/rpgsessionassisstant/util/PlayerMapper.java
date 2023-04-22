package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.PlayerDto;
import com.zpsm.rpgsessionassisstant.model.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PlayerMapper {

    private final CharacterMapper characterMapper;
    private final GamemasterMapper gamemasterMapper;

    public PlayerDto mapToDto(Player player) {
        return new PlayerDto(
            player.getId(),
            player.getLogin(),
            player.getGamemasters()
                .stream()
                .map(gamemasterMapper::mapToDto)
                .collect(Collectors.toSet()),
            player.getCharacters()
                .stream()
                .map(characterMapper::mapToDto)
                .collect(Collectors.toSet()));
    }

}
