package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import com.zpsm.rpgsessionassisstant.repository.ItemRepository;
import com.zpsm.rpgsessionassisstant.util.ItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void givenPageableShouldReturnPageOfItems() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Item> itemList = getItems();
        List<ItemDto> dtoList = List.of(new ItemDto(1L, "Sword", "Desc", Set.of()),
            new ItemDto(2L, "Gun", "Gun", Set.of()));
        PageImpl<Item> items = new PageImpl<>(itemList, pageRequest, itemList.size());
        PageImpl<ItemDto> expected = new PageImpl<>(dtoList, pageRequest, dtoList.size());
        when(mockiItemRepository.findAll(eq(pageRequest))).thenReturn(items);
        when(mockItemMapper.mapToDto(any())).thenReturn(dtoList.get(0), dtoList.get(1));

        // when
        Page<ItemDto> actual = itemService.getPage(pageRequest);

        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenCharacterAndExistingItemIdWhenAddingItemShouldAddItemToCharacter() {
        // given
        Item item = new Item();
        Character character = new Character();
        character.addItem(item);
        when(mockiItemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // when
        Character actual = itemService.addItemToCharacter(new Character(), 1L);

        // then
        assertFalse(actual.getItems().isEmpty());
    }

    @Test
    void givenNullCharacterWhenAddingItemShouldThrowEntityNotFoundException() {
        // given // then // then
        assertThrows(EntityNotFoundException.class,
            () -> itemService.addItemToCharacter(null, 1L));
    }

    @Test
    void givenCharacterAndExistingItemIdWhenRemovingItemShouldRemoveItemFromCharacter() {
        // given
        Item item = new Item();
        Character character = new Character();
        character.addItem(item);
        when(mockiItemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // when
        Character actual = itemService.removeItemFromCharacter(character, 1L);

        // then
        assertTrue(actual.getItems().isEmpty());
    }

    @Test
    void givenNullCharacterWhenRemovingItemShouldThrowEntityNotFoundException() {
        // given // then // then
        assertThrows(EntityNotFoundException.class,
            () -> itemService.removeItemFromCharacter(null, 1L));
    }

    private List<Item> getItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Sword");
        item1.setDescription("Sword");
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Gun");
        item2.setDescription("Gun");
        return List.of(item1, item2);
    }

}
