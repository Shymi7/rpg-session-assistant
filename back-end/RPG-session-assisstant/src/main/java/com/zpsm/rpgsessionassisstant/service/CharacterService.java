package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.AddOrRemoveFromCharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CreateCharacterDto;
import com.zpsm.rpgsessionassisstant.dto.ModifyCharactersAttributesDto;
import com.zpsm.rpgsessionassisstant.exception.CharacterNotInAnyRoomException;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.ModifyingAttributesException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.CharacterRepository;
import com.zpsm.rpgsessionassisstant.util.CharacterMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    private final PlayerDetailsService playerDetailsService;
    private final ItemService itemService;
    private final QuestService questService;
    private final AttributeService attributeService;

    public CharacterDto getCharacterById(Long id) {
        return characterMapper.mapToDto(getCharacter(id));
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
        Character character = prepareCharacter(dto.name(), dto.description());
        character.setPlayer(player);
        Character saved = characterRepository.save(character);
        player.getCharacters().add(saved);
        playerDetailsService.save(player);
        Set<CharacterAttribute> savedCharacterAttributes = attributeService.saveCharacterAttributes(saved, dto.attributeNames());
        saved.getCharacterAttributes().addAll(savedCharacterAttributes);
        saved = characterRepository.save(saved);
        return characterMapper.mapToDto(saved);
    }

    @Transactional
    public CharacterDto addItem(AddOrRemoveFromCharacterDto dto) {
        Character character = getCharacter(dto.characterId());
        Character characterWithItem = itemService.addItemToCharacter(character, dto.entityId());
        Character savedCharacter = characterRepository.save(characterWithItem);
        return characterMapper.mapToDto(savedCharacter);
    }

    @Transactional
    public CharacterDto removeItem(AddOrRemoveFromCharacterDto dto) {
        Character foundCharacter = getCharacter(dto.characterId());
        Character characterWithItem = itemService.removeItemFromCharacter(foundCharacter, dto.entityId());
        return characterMapper.mapToDto(characterRepository.save(characterWithItem));
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

    public CharacterDto modifyCharactersAttribute(Long characterId, ModifyCharactersAttributesDto dto) {
        Character character = getCharacter(characterId);
        canModifyCharactersAttributes(character, dto);
        attributeService.updateCharacterAttribute(characterId, dto);
        character.setSkillPoints(character.getSkillPoints() - dto.skillPoints());
        return characterMapper.mapToDto(characterRepository.save(character));
    }

    private Character prepareCharacter(String name, String description) {
        Character character = new Character();
        character.setName(name);
        character.setHealth(100);
        character.setLevel(1);
        character.setSkillPoints(0);
        character.setExperience(0);
        character.setDescription(description);
        return character;
    }

    private Character getCharacter(long characterId) {
        return characterRepository.findById(characterId)
            .orElseThrow(() -> {
                log.error("Character with id {} not found", characterId);
                return new EntityNotFoundException(String.format("Character with id %d not found", characterId));
            });
    }

    private boolean doesCharacterHasGivenAttributes(Long characterId, Long attributeId) {
        return characterRepository.doesCharacterHasGivenAttributes(characterId, attributeId)
            .isPresent();
    }

    private void canModifyCharactersAttributes(Character character, ModifyCharactersAttributesDto dto) {
        if (!doesCharacterHasGivenAttributes(character.getId(), dto.attributeId())) {
            log.error("Character doesn't have one or more provided attributes");
            throw new ModifyingAttributesException("Character doesn't has one or more provided attributes");
        }
        if (character.getSkillPoints() < dto.skillPoints()) {
            log.error("Character doesn't have enough skill points");
            throw new ModifyingAttributesException("Character doesn't have enough skill points");
        }
    }

}
