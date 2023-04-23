package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import com.zpsm.rpgsessionassisstant.repository.ItemRepository;
import com.zpsm.rpgsessionassisstant.util.ItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository mockiItemRepository;
    @Mock
    private ItemMapper mockItemMapper;
    @Mock
    private AttributeService mockAttributeService;
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemService(mockiItemRepository, mockItemMapper, mockAttributeService);
    }

    @Test
    void givenExistingIdShouldReturnItemDto() {
        // given
        Item item = new Item();
        item.setId(1L);
        item.setName("Ulu-Mulu");
        item.setDescription("Sacred weapon of orks");
        item.setItemAttributes(Set.of());
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Price");
        ItemAttribute itemAttribute = new ItemAttribute();
        itemAttribute.setAttribute(attribute);
        itemAttribute.setAttributeValue(300);
        itemAttribute.setAttribute(attribute);
        ItemDto expected = new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            Set.of(new ItemAttributeDto(new AttributeDto(attribute.getId(), attribute.getName()),
                itemAttribute.getAttributeValue())));
        when(mockiItemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mockItemMapper.mapToDto(any())).thenReturn(expected);

        // when
        ItemDto actual = itemService.getById(item.getId());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingItemIdShouldThrowItemException() {
        // given
        when(mockiItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> itemService.getById(1L));
    }

    @Test
    void givenExistingNameShouldReturnItemDto() {
        // given
        Item item = new Item();
        item.setId(1L);
        item.setName("Ulu-Mulu");
        item.setDescription("Sacred weapon of orks");
        item.setItemAttributes(Set.of());
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Price");
        ItemAttribute itemAttribute = new ItemAttribute();
        itemAttribute.setAttribute(attribute);
        itemAttribute.setAttributeValue(300);
        itemAttribute.setAttribute(attribute);
        ItemDto expected = new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            Set.of(new ItemAttributeDto(new AttributeDto(attribute.getId(), attribute.getName()),
                itemAttribute.getAttributeValue())));
        when(mockiItemRepository.findByName(anyString())).thenReturn(Optional.of(item));
        when(mockItemMapper.mapToDto(any())).thenReturn(expected);

        // when
        ItemDto actual = itemService.getByName(item.getName());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingItemNameShouldThrowItemException() {
        // given
        when(mockiItemRepository.findByName(anyString())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> itemService.getByName("asdf"));
    }

    @Test
    void givenCorrectDtoShouldCreateNewItemWithAttributes() {
        // given
        CreateItemAttributeDto damageDto = new CreateItemAttributeDto(new CreateNewAttributeDto("Damage"), 300);
        CreateItemAttributeDto durationDto = new CreateItemAttributeDto(new CreateNewAttributeDto("Duration"), 30);
        CreateItemDto createItemDto = new CreateItemDto(
            "Fists of Fury",
            "Gives the temporal boost to damage from fists",
            Set.of(damageDto, durationDto));
        Item saved = new Item();
        saved.setId(1L);
        saved.setName(createItemDto.name());
        saved.setDescription(createItemDto.description());

        ItemAttribute damage = new ItemAttribute();
        Attribute damageAttribute = new Attribute();
        damageAttribute.setName(damageDto.attribute().name());
        damageAttribute.setId(1L);
        damage.setAttribute(damageAttribute);
        damage.setAttributeValue(damageDto.attributeValue());

        ItemAttribute duration = new ItemAttribute();
        Attribute durationAttribute = new Attribute();
        durationAttribute.setId(2L);
        durationAttribute.setName(durationDto.attribute().name());
        duration.setAttribute(durationAttribute);
        duration.setAttributeValue(damageDto.attributeValue());

        Item savedAtTheEnd = new Item();
        savedAtTheEnd.setId(1L);
        savedAtTheEnd.setItemAttributes(Set.of(damage, duration));
        savedAtTheEnd.setName(createItemDto.name());
        savedAtTheEnd.setDescription(createItemDto.description());

        ItemDto expected = new ItemDto(
            saved.getId(),
            saved.getName(),
            saved.getName(),
            Set.of(new ItemAttributeDto(new AttributeDto(damage.getAttribute().getId(), damage.getAttribute().getName()), damage.getAttributeValue()),
                new ItemAttributeDto(new AttributeDto(duration.getAttribute().getId(), duration.getAttribute().getName()), duration.getAttributeValue())));
        when(mockiItemRepository.save(any())).thenReturn(saved, savedAtTheEnd);
        when(mockAttributeService.createNewItemAttribute(any(), eq(damageDto.attribute().name()), eq(damageDto.attributeValue())))
            .thenReturn(damage);
        when(mockAttributeService.createNewItemAttribute(any(), eq(durationDto.attribute().name()), eq(durationDto.attributeValue())))
            .thenReturn(duration);
        when(mockItemMapper.mapToDto(any())).thenReturn(expected);

        // when
        ItemDto actual = itemService.create(createItemDto);

        // then
        assertEquals(expected, actual);
    }

}
