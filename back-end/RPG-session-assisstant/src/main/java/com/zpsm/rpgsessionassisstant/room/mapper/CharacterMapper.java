package com.zpsm.rpgsessionassisstant.room.mapper;

import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.Quest;
import com.zpsm.rpgsessionassisstant.room.dto.CharacterDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CharacterMapper {

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
