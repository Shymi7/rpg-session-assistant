package com.zpsm.rpgsessionassisstant.attribute.mapper;

import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.room.dto.AttributeDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributeMapperTest {

    private final AttributeMapper attributeMapper = new AttributeMapper();

    @Test
    void givenAttributeEntityShouldMapToDTO() {
        // given
        Attribute attribute = new Attribute();
        attribute.setId(2L);
        attribute.setName("Strength");
        AttributeDto expected = new AttributeDto(attribute.getId(), attribute.getName());

        // when
        AttributeDto actual = attributeMapper.mapToDto(attribute);

        // then
        assertEquals(expected, actual);
    }

}
