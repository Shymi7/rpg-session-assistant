package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CreateCharacterDto;
import com.zpsm.rpgsessionassisstant.exception.CharacterException;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.RoomException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import com.zpsm.rpgsessionassisstant.repository.*;
import com.zpsm.rpgsessionassisstant.util.CharacterMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final AttributeRepository attributeRepository;
    private final CharacterAttributeRepository characterAttributeRepository;
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final CharacterMapper characterMapper;

    public CharacterDto getCharacterById(Long id) {
        return characterRepository.findById(id)
            .map(characterMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Character with id {} doesn't exist", id);
                return new CharacterException(String.format("Character with id %d doesn't exist", id));
            });
    }

    public Collection<CharacterDto> getPlayersCharacters(Long id) {
        return characterRepository.findAllPlayersCharacters(id)
            .stream()
            .map(characterMapper::mapToDto)
            .toList();
    }

    public CharacterDto createCharacter(CreateCharacterDto dto, Principal principal) {
        Player player = playerRepository.findByLogin(principal.getName())
            .orElseThrow(() -> new PlayerNotFoundException(
                String.format("Player with login %s not found", principal.getName())));
        Character character = prepareCharacter(dto.name(), dto.roomId());
        character.setPlayer(player);
        Character saved = characterRepository.save(character);
        player.getCharacters().add(saved);
        playerRepository.save(player);
        Set<CharacterAttribute> savedCharacterAttributes = saveCharacterAttributes(saved, dto.attributeNames());
        saved.setCharacterAttributes(savedCharacterAttributes);
        saved = characterRepository.save(saved);
        return characterMapper.mapToDto(saved);
    }

    private Character prepareCharacter(String name, long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> {
                log.error("Room with id {} not found", roomId);
                return new RoomException(String.format("Room with id %d not found", roomId));
            });
        Character character = new Character();
        character.setName(name);
        character.setHealth(100);
        character.setLevel(1);
        character.setSkillPoints(0);
        character.setExperience(0);
        character.getRooms().add(room);
        return character;
    }

    private Set<CharacterAttribute> saveCharacterAttributes(Character character, List<String> attributeNames) {
        List<Attribute> attributes = attributeRepository.findAllByNameIn(attributeNames);
        Set<CharacterAttribute> characterAttributes = new LinkedHashSet<>();
        attributes.forEach(attribute -> {
            CharacterAttribute characterAttribute = new CharacterAttribute();
            characterAttribute.setCharacter(character);
            characterAttribute.setAttribute(attribute);
            characterAttributes.add(characterAttribute);
        });
        return new HashSet<>(characterAttributeRepository.saveAll(characterAttributes));
    }

}
