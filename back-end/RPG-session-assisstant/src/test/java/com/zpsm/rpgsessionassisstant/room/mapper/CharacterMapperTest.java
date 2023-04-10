package com.zpsm.rpgsessionassisstant.room.mapper;

import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterMapperTest {

    private CharacterMapper characterMapper = new CharacterMapper();

    @Test
    void givenCharacterEntityShouldMapToDTO() {
        // given
        Character character = getCharacter();
        CharacterDto expected = getCharacterDto();

        // when
        CharacterDto actual = characterMapper.mapToDto(character);

        // then
        assertEquals(expected, actual);
    }

    private Character getCharacter() {
        CharacterAttributeKey characterAttributeKey = new CharacterAttributeKey();
        characterAttributeKey.setCharacterId(1L);
        characterAttributeKey.setAttributeId(1L);
        CharacterAttribute characterAttribute = new CharacterAttribute();
        characterAttribute.setCharacterAttributeKey(characterAttributeKey);
        Item item = new Item();
        item.setId(1L);
        Quest quest = new Quest();
        quest.setId(1L);
        Character character = new Character();
        character.setId(1L);
        character.setLevel(4);
        character.setName("Test");
        character.setHealth(100);
        character.setSkillPoints(22);
        character.setExperience(300);
        character.setCharacterAttributes(Set.of(characterAttribute));
        character.setItems(Set.of(item));
        character.setQuests(Set.of(quest));
        return character;
    }

    private CharacterDto getCharacterDto() {
        return new CharacterDto(
            1L,
            "Test",
            4,
            100,
            22,
            300,
            Set.of(1L),
            Set.of(1L),
            Set.of(1L));
    }

}
