package com.zpsm.rpgsessionassisstant.item.mapper;

import com.zpsm.rpgsessionassisstant.attribute.AttributeMapper;
import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.ItemAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
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
class ItemMapperTest {

    @Mock
    private AttributeMapper mockAttributeMapper;
    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapper(mockAttributeMapper);
    }

    @Test
    void givenItemEntityShouldReturnDTO() {
        // given
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Durability");
        ItemAttribute itemAttribute = new ItemAttribute();
        itemAttribute.setAttributeValue(3);
        itemAttribute.setAttribute(attribute);
        attribute.getItemAttributes().add(itemAttribute);
        Item item = new Item();
        item.setId(33L);
        item.setName("Ulu-Mulu");
        item.setDescription("Item from Gothic");
        item.getItemAttributes().add(itemAttribute);
        when(mockAttributeMapper.mapToItemAttributeDto(any()))
            .thenReturn(new ItemAttributeDto(new AttributeDto(1L, "Durability"), 3));

        // when
        ItemDto actual = itemMapper.mapToDto(item);

        // then
        assertEquals(getItemDto(), actual);
    }

    private ItemDto getItemDto() {
        return new ItemDto(
            33L,
            "Ulu-Mulu",
            "Item from Gothic",
            Set.of(new ItemAttributeDto(new AttributeDto(1L, "Durability"), 3)));
    }

}
