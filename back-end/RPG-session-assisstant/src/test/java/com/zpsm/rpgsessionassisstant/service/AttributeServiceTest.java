package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CreateNewAttributeDto;
import com.zpsm.rpgsessionassisstant.exception.AttributeException;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import com.zpsm.rpgsessionassisstant.model.ItemAttributeKey;
import com.zpsm.rpgsessionassisstant.repository.AttributeRepository;
import com.zpsm.rpgsessionassisstant.repository.ItemAttributeRepository;
import com.zpsm.rpgsessionassisstant.util.AttributeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeRepository mockAttributeRepository;
    @Mock
    private AttributeMapper mockAttributeMapper;
    @Mock
    private ItemAttributeRepository mockItemAttributeRepository;
    private AttributeService attributeService;

    @BeforeEach
    void setUp() {
        attributeService = new AttributeService(mockAttributeRepository, mockItemAttributeRepository, mockAttributeMapper);
    }

    @Test
    void givenCorrectNameShouldReturnAttributeDTO() {
        // given
        String attributeName = "Strength";
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName(attributeName);
        AttributeDto expected = new AttributeDto(attribute.getId(), attributeName);
        when(mockAttributeRepository.findByName(anyString())).thenReturn(Optional.of(attribute));
        when(mockAttributeMapper.mapToAttributeDto(any())).thenReturn(expected);

        // when
        AttributeDto actual = attributeService.getAttributeByName(attributeName);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingNameShouldThrowAttributeException() {
        // given
        String attributeName = "Strength";
        when(mockAttributeRepository.findByName(anyString())).thenReturn(Optional.empty());

        // when // then
        assertThrows(AttributeException.class, () -> attributeService.getAttributeByName(attributeName));
    }

    @Test
    void shouldReturnCollectionOfAttributeDTOs() {
        // given
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        attribute1.setName("Strength");
        Attribute attribute2 = new Attribute();
        attribute2.setId(1L);
        attribute2.setName("Strength");
        List<Attribute> attributes = List.of(attribute1, attribute2);
        List<AttributeDto> expected = List.of(
            new AttributeDto(attribute1.getId(), attribute1.getName()),
            new AttributeDto(attribute2.getId(), attribute2.getName()));
        when(mockAttributeRepository.findAll()).thenReturn(attributes);
        when(mockAttributeMapper.mapToAttributeDto(attribute1)).thenReturn(expected.get(0));
        when(mockAttributeMapper.mapToAttributeDto(attribute2)).thenReturn(expected.get(1));

        // when
        Collection<AttributeDto> actual = attributeService.getAllAttributes();

        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenNoAttributesInDatabaseShouldReturnEmptyCollection() {
        // given
        when(mockAttributeRepository.findAll()).thenReturn(List.of());

        // when
        Collection<AttributeDto> actual = attributeService.getAllAttributes();

        // then
        assertIterableEquals(List.of(), actual);
    }

    @Test
    void givenExistingIdShouldReturnAttributeDTO() {
        // given
        long id = 1L;
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setName("Strength");
        AttributeDto expected = new AttributeDto(attribute.getId(), attribute.getName());
        when(mockAttributeRepository.findById(anyLong())).thenReturn(Optional.of(attribute));
        when(mockAttributeMapper.mapToAttributeDto(any())).thenReturn(expected);

        // when
        AttributeDto actual = attributeService.getAttributeById(id);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdShouldThrowAttributeException() {
        // given
        long id = 1L;
        when(mockAttributeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(AttributeException.class, () -> attributeService.getAttributeById(id));
    }

    @Test
    void givenCorrectDTOShouldCreateNewAttribute() {
        // given
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Strength");
        AttributeDto expected = new AttributeDto(attribute.getId(), attribute.getName());
        when(mockAttributeRepository.save(any())).thenReturn(attribute);
        when(mockAttributeMapper.mapToAttributeDto(any())).thenReturn(expected);

        // when
        AttributeDto actual = attributeService.createNewAttribute(new CreateNewAttributeDto(attribute.getName()));

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenItemAndExistingAttributeShouldCreateItemAttribute() {
        // given
        Item item = new Item();
        item.setId(1L);
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Price");
        ItemAttribute expected = new ItemAttribute();
        expected.setAttribute(attribute);
        expected.setItem(item);
        expected.setAttributeValue(4);
        ItemAttributeKey itemAttributeKey = new ItemAttributeKey();
        itemAttributeKey.setAttributeId(attribute.getId());
        itemAttributeKey.setItemId(item.getId());
        expected.setId(itemAttributeKey);
        when(mockAttributeRepository.findByName(anyString())).thenReturn(Optional.of(attribute));
        when(mockItemAttributeRepository.save(any())).thenReturn(expected);

        // when
        ItemAttribute actual = attributeService.createNewItemAttribute(item, attribute.getName(), expected.getAttributeValue());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNullItemShouldThrowAttributeException() {
        // given // when // then
        assertThrows(AttributeException.class,
            () -> attributeService.createNewItemAttribute(null, "Strength", 6));
    }

    @Test
    void givenNonExistingAttributeShouldThrowAttributeException() {
        // given
        Item item = new Item();
        when(mockAttributeRepository.findByName(anyString())).thenReturn(Optional.empty());

        // when // then
        assertThrows(AttributeException.class,
            () -> attributeService.createNewItemAttribute(item, "Strength", 6));
    }

}
