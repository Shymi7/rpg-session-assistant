package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.AddOrRemoveFromCharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CreateCharacterDto;
import com.zpsm.rpgsessionassisstant.exception.CharacterNotInAnyRoomException;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import com.zpsm.rpgsessionassisstant.repository.AttributeRepository;
import com.zpsm.rpgsessionassisstant.repository.CharacterAttributeRepository;
import com.zpsm.rpgsessionassisstant.repository.CharacterRepository;
import com.zpsm.rpgsessionassisstant.util.CharacterMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final AttributeRepository attributeRepository;
    private final CharacterAttributeRepository characterAttributeRepository;
    private final CharacterMapper characterMapper;
    private final PlayerDetailsService playerDetailsService;
    private final ItemService itemService;
    private final QuestService questService;

    public CharacterDto getCharacterById(Long id) {
        return characterRepository.findById(id)
            .map(characterMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Character with id {} doesn't exist", id);
                return new EntityNotFoundException(String.format("Character with id %d doesn't exist", id));
            });
    }

    public Collection<CharacterDto> getPlayersCharacters(Long id) {
        return characterRepository.findAllPlayersCharacters(id)
            .stream()
            .map(characterMapper::mapToDto)
            .toList();
    }

    @Transactional
    public CharacterDto createCharacter(CreateCharacterDto dto, Principal principal) {
        Player player = (Player) playerDetailsService.loadUserByUsername(principal.getName());
        Character character = prepareCharacter(dto.name());
        character.setPlayer(player);
        Character saved = characterRepository.save(character);
        player.getCharacters().add(saved);
        playerDetailsService.save(player);
        Set<CharacterAttribute> savedCharacterAttributes = saveCharacterAttributes(saved, dto.attributeNames());
        saved.setCharacterAttributes(savedCharacterAttributes);
        saved = characterRepository.save(saved);
        return characterMapper.mapToDto(saved);
    }

    @Transactional
    public CharacterDto addItem(AddOrRemoveFromCharacterDto dto) {
        Character character = getCharacter(dto.characterId());
        Item item = itemService.getItem(dto.entityId());
        character.addItem(item);
        Character savedCharacter = characterRepository.save(character);
        return characterMapper.mapToDto(savedCharacter);
    }

    @Transactional
    public CharacterDto removeItem(AddOrRemoveFromCharacterDto dto) {
        Character foundCharacter = getCharacter(dto.characterId());
        Item foundItem = itemService.getItem(dto.entityId());
        foundCharacter.removeItem(foundItem);
        return characterMapper.mapToDto(characterRepository.save(foundCharacter));
    }

    public Character getCharacter(long characterId) {
        return characterRepository.findById(characterId)
            .orElseThrow(() -> {
                log.error("Character with id {} not found", characterId);
                return new EntityNotFoundException(String.format("Character with id %d not found", characterId));
            });
    }

    @Transactional
    public void addQuest(AddOrRemoveFromCharacterDto dto) {
        Character character = getCharacter(dto.characterId());
        if (character.getRooms().isEmpty()) {
            log.error("Cannot add quest to a character which doesn't belong to any room");
            throw new CharacterNotInAnyRoomException("Cannot add quest to a character which doesn't belong to any room");
        }
        character = questService.addQuestToCharacter(character, dto.entityId());
        characterRepository.save(character);
    }

    @Transactional
    public void removeQuest(AddOrRemoveFromCharacterDto dto) {
        Character character = getCharacter(dto.characterId());
        if (character.getRooms().isEmpty()) {
            log.error("Cannot remove quest from a character which doesn't belong to any room");
            throw new CharacterNotInAnyRoomException("Cannot remove quest from a character which doesn't belong to any room");
        }
        character = questService.removeQuestFromCharacter(character, dto.entityId());
        characterRepository.save(character);
    }

    private Character prepareCharacter(String name) {
        Character character = new Character();
        character.setName(name);
        character.setHealth(100);
        character.setLevel(1);
        character.setSkillPoints(0);
        character.setExperience(0);
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
