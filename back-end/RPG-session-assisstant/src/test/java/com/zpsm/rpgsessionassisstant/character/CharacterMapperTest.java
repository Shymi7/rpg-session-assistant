package com.zpsm.rpgsessionassisstant.character;

import com.zpsm.rpgsessionassisstant.attribute.AttributeMapper;
import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.item.mapper.ItemMapper;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterMapperTest {

    @Mock
    private ItemMapper mockItemMapper;
    @Mock
    private AttributeMapper mockAttributeMapper;
    private CharacterMapper characterMapper;

    @BeforeEach
    void setUp() {
        characterMapper = new CharacterMapper(mockItemMapper, mockAttributeMapper);
    }

    @Test
    void givenCharacterEntityShouldMapToDTO() {
        // given
        Character character = getCharacter();
        CharacterDto expected = getCharacterDto();
        when(mockItemMapper.mapToDto(any()))
            .thenReturn(new ItemDto(1L, "Sword", "", Set.of()));
        when(mockAttributeMapper.mapToCharacterAttributeDto(any()))
            .thenReturn(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1));

        // when
        CharacterDto actual = characterMapper.mapToDto(character);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenCharacterEntityShouldMapToOtherDTO() {
        // given
        Character character = getCharacter();
        CharacterDto expected = getCharacterDto1();
        when(mockItemMapper.mapToDto(any()))
            .thenReturn(new ItemDto(1L, "Master Sword", "Sword", Set.of()));
        when(mockAttributeMapper.mapToCharacterAttributeDto(any()))
            .thenReturn(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1));

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
            Set.of(new ItemDto(1L, "Sword", "", Set.of())),
            Set.of(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1)));
    }
    private CharacterDto getCharacterDto1() {
        return new CharacterDto(
            1L,
            "Test",
            4,
            100,
            22,
            300,
            Set.of(new ItemDto(1L, "Master Sword", "Sword", Set.of())),
            Set.of(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1)));
    }


}
