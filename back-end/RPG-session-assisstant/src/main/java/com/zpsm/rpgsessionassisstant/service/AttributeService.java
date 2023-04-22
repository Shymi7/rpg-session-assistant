package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CreateNewAttributeDto;
import com.zpsm.rpgsessionassisstant.exception.AttributeException;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import com.zpsm.rpgsessionassisstant.repository.AttributeRepository;
import com.zpsm.rpgsessionassisstant.repository.ItemAttributeRepository;
import com.zpsm.rpgsessionassisstant.util.AttributeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final ItemAttributeRepository itemAttributeRepository;
    private final AttributeMapper attributeMapper;

    public AttributeDto getAttributeByName(String name) {
        return attributeRepository.findByName(name)
            .map(attributeMapper::mapToAttributeDto)
            .orElseThrow(() -> {
                log.error("Attribute with name {} doesn't exits", name);
                return new AttributeException(String.format("Attribute with name %s doesn't exits", name));
            });
    }

    public Collection<AttributeDto> getAllAttributes() {
        return attributeRepository.findAll()
            .stream()
            .map(attributeMapper::mapToAttributeDto)
            .toList();
    }

    public AttributeDto getAttributeById(Long id) {
        return attributeRepository.findById(id)
            .map(attributeMapper::mapToAttributeDto)
            .orElseThrow(() -> {
                log.error("Attribute with id {} doesn't exits", id);
                return new AttributeException(String.format("Attribute with id %d doesn't exits", id));
            });
    }

    public AttributeDto createNewAttribute(CreateNewAttributeDto dto) {
        Attribute attribute = new Attribute();
        attribute.setName(dto.name());
        Attribute saved = attributeRepository.save(attribute);
        log.info("New attribute created");
        return attributeMapper.mapToAttributeDto(saved);
    }

    public ItemAttribute createNewItemAttribute(Item item, String attributeName, int attributeValue) {
        if (null == item) {
            log.error("Item to bind ItemAttribute to is null");
            throw new AttributeException("Item to bind ItemAttribute to is null");
        }
        Attribute attribute = attributeRepository.findByName(attributeName)
            .orElseThrow(() -> {
                log.error("Attribute with name {} doesn't exist", attributeName);
                return new AttributeException(String.format("Attribute with name %s doesn't exist", attributeName));
            });
        ItemAttribute newItemAttribute = new ItemAttribute();
        newItemAttribute.setItem(item);
        newItemAttribute.setAttribute(attribute);
        newItemAttribute.setAttributeValue(attributeValue);
        return itemAttributeRepository.save(newItemAttribute);
    }

}
