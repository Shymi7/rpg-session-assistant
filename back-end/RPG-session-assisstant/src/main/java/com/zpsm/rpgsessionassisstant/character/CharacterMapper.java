package com.zpsm.rpgsessionassisstant.character;

import com.zpsm.rpgsessionassisstant.attribute.AttributeMapper;
import com.zpsm.rpgsessionassisstant.dto.CharacterAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.item.mapper.ItemMapper;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.Item;
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
            mapCharacterAttributes(character.getCharacterAttributes()));
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

}
