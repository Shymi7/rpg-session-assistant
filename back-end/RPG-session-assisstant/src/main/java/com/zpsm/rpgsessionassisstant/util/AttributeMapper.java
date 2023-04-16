package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.ItemAttributeDto;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.CharacterAttribute;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper {

    public AttributeDto mapToAttributeDto(Attribute attribute) {
        return new AttributeDto(attribute.getId(), attribute.getName());
    }

    public ItemAttributeDto mapToItemAttributeDto(ItemAttribute itemAttribute) {
        return new ItemAttributeDto(mapToAttributeDto(itemAttribute.getAttribute()), itemAttribute.getAttributeValue());
    }

    public CharacterAttributeDto mapToCharacterAttributeDto(CharacterAttribute characterAttribute) {
        return new CharacterAttributeDto(
            mapToAttributeDto(characterAttribute.getAttribute()),
            characterAttribute.getAttributeLevel());
    }

}
