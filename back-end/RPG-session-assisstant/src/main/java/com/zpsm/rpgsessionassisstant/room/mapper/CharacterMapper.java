package com.zpsm.rpgsessionassisstant.room.mapper;

import com.zpsm.rpgsessionassisstant.attribute.mapper.AttributeMapper;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto1;
import com.zpsm.rpgsessionassisstant.item.mapper.ItemMapper;
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

    public CharacterDto mapToDto(Character character) {
        return new CharacterDto(
            character.getId(),
            character.getName(),
            character.getLevel(),
            character.getHealth(),
            character.getSkillPoints(),
            character.getExperience(),
            mapItems(character.getItems()),
            mapQuests(character.getQuests()),
            mapCharacterAttributes(character.getCharacterAttributes()));
    }

    public CharacterDto1 mapToOtherDTO(Character character) {
        return new CharacterDto1(
            character.getId(),
            character.getName(),
            character.getLevel(),
            character.getHealth(),
            character.getSkillPoints(),
            character.getExperience(),
            character.getItems()
                .stream()
                .map(itemMapper::mapToDto)
                .collect(Collectors.toSet()),
            character.getCharacterAttributes()
                .stream()
                .map(attributeMapper::mapToCharacterAttributeDto)
                .collect(Collectors.toSet())
        );
    }

    private Set<Long> mapItems(Set<Item> items) {
        return items.stream()
            .map(Item::getId)
            .collect(Collectors.toSet());
    }

    private Set<Long> mapQuests(Set<Quest> quests) {
        return quests.stream()
            .map(Quest::getId)
            .collect(Collectors.toSet());
    }

    private Set<Long> mapCharacterAttributes(Set<CharacterAttribute> characterAttributes) {
        return characterAttributes.stream()
            .map(characterAttribute -> characterAttribute.getCharacterAttributeKey().getAttributeId())
            .collect(Collectors.toSet());
    }

}
