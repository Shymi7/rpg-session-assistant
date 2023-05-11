package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.CharacterAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.dto.QuestDto;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.Quest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CharacterMapper {

    private final ItemMapper itemMapper;
    private final AttributeMapper attributeMapper;
    private final QuestMapper questMapper;

    public CharacterDto mapToDto(Character character) {
        return new CharacterDto(
            character.getId(),
            character.getName(),
            character.getLevel(),
            character.getHealth(),
            character.getSkillPoints(),
            character.getExperience(),
            mapItems(character.getItems()),
            mapCharacterAttributes(character.getCharacterAttributes()),
            mapQuests(character.getQuests()));
    }

    private Set<ItemDto> mapItems(Set<Item> items) {
        return items.stream()
            .map(itemMapper::mapToDto)
            .collect(Collectors.toSet());
    }

    private Set<CharacterAttributeDto> mapCharacterAttributes(Set<CharacterAttribute> characterAttributes) {
        return characterAttributes.stream()
            .map(attributeMapper::mapToCharacterAttributeDto)
            .collect(Collectors.toSet());
    }

    private Set<QuestDto> mapQuests(Set<Quest> quests) {
        return quests.stream()
            .map(questMapper::mapToDto)
            .collect(Collectors.toSet());
    }

}
