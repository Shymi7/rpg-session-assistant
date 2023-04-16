package com.zpsm.rpgsessionassisstant.attribute.mapper;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.ItemAttributeDto;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributeMapperTest {

    private final AttributeMapper attributeMapper = new AttributeMapper();

    @Test
    void givenAttributeEntityShouldMapToAttributeDto() {
        // given
        Attribute attribute = getAttribute("Strength");
        AttributeDto expected = new AttributeDto(attribute.getId(), attribute.getName());

        // when
        AttributeDto actual = attributeMapper.mapToAttributeDto(attribute);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenItemAttributeEntityShouldMapToItemAttributeDto() {
        // given
        Item item = new Item();
        item.setId(1L);
        item.setName("Master Sword");
        Attribute attribute = getAttribute("Damage");
        ItemAttribute itemAttribute = new ItemAttribute();
        itemAttribute.setAttribute(attribute);
        itemAttribute.setAttributeValue(330);
        itemAttribute.setItem(item);
        item.getItemAttributes().add(itemAttribute);
        ItemAttributeDto expected = new ItemAttributeDto(attributeMapper.mapToAttributeDto(attribute), itemAttribute.getAttributeValue());

        // when
        ItemAttributeDto actual = attributeMapper.mapToItemAttributeDto(itemAttribute);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenCharacterAttributeEntityShouldMapToCharacterAttributeDto() {
        // given
        Attribute attribute = getAttribute("Strength");
        CharacterAttribute characterAttribute = new CharacterAttribute();
        characterAttribute.setAttribute(attribute);
        characterAttribute.setAttributeLevel(3);
        CharacterAttributeDto expected = new CharacterAttributeDto(
            attributeMapper.mapToAttributeDto(attribute),
            characterAttribute.getAttributeLevel());

        // when
        CharacterAttributeDto actual = attributeMapper.mapToCharacterAttributeDto(characterAttribute);

        // then
        assertEquals(expected, actual);
    }

    private Attribute getAttribute(String name) {
        Attribute attribute = new Attribute();
        attribute.setId(2L);
        attribute.setName(name);
        return attribute;
    }

}
