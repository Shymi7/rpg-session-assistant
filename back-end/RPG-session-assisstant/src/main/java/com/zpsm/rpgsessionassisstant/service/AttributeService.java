package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CreateNewAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.ModifyCharactersAttributesDto;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import com.zpsm.rpgsessionassisstant.repository.AttributeRepository;
import com.zpsm.rpgsessionassisstant.repository.CharacterAttributeRepository;
import com.zpsm.rpgsessionassisstant.repository.ItemAttributeRepository;
import com.zpsm.rpgsessionassisstant.util.AttributeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final ItemAttributeRepository itemAttributeRepository;
    private final CharacterAttributeRepository characterAttributeRepository;
    private final AttributeMapper attributeMapper;

    public AttributeDto getAttributeByName(String name) {
        return attributeRepository.findByName(name)
            .map(attributeMapper::mapToAttributeDto)
            .orElseThrow(() -> {
                log.error("Attribute with name {} doesn't exits", name);
                return new EntityNotFoundException(String.format("Attribute with name %s doesn't exits", name));
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
                return new EntityNotFoundException(String.format("Attribute with id %d doesn't exits", id));
            });
    }

    public AttributeDto createNewAttribute(CreateNewAttributeDto dto) {
        Attribute attribute = new Attribute();
        attribute.setName(dto.name());
        Attribute saved = attributeRepository.save(attribute);
        log.info("New attribute created");
        return attributeMapper.mapToAttributeDto(saved);
    }

    @Transactional
    public ItemAttribute createNewItemAttribute(Item item, String attributeName, int attributeValue) {
        if (null == item) {
            log.error("Item to bind ItemAttribute to is null");
            throw new EntityNotFoundException("Item to bind ItemAttribute to is null");
        }
        Attribute attribute = attributeRepository.findByName(attributeName)
            .orElseThrow(() -> {
                log.error("Attribute with name {} doesn't exist", attributeName);
                return new EntityNotFoundException(String.format("Attribute with name %s doesn't exist", attributeName));
            });
        ItemAttribute newItemAttribute = new ItemAttribute();
        newItemAttribute.setItem(item);
        newItemAttribute.setAttribute(attribute);
        newItemAttribute.setAttributeValue(attributeValue);
        return itemAttributeRepository.save(newItemAttribute);
    }

    public Set<CharacterAttribute> saveCharacterAttributes(Character character, List<String> attributeNames) {
        if (attributeNames.isEmpty()) {
            return Set.of();
        }
        if (null == character) {
            log.error("Character was null");
            throw new EntityNotFoundException("Character was null");
        }
        List<Attribute> attributes = attributeRepository.findAllByNameIn(attributeNames);
        Set<CharacterAttribute> characterAttributes = new LinkedHashSet<>();
        attributes.forEach(attribute -> {
            CharacterAttribute characterAttribute = new CharacterAttribute();
            characterAttribute.setCharacter(character);
            characterAttribute.setAttribute(attribute);
            characterAttributes.add(characterAttribute);
        });
        return new HashSet<>(characterAttributeRepository.saveAll(characterAttributes));
    }

    @Transactional
    public int updateCharacterAttribute(
        Long characterId,
        ModifyCharactersAttributesDto.AttributeLevelPair attributeLevelPair) {

        return characterAttributeRepository.updateAttributeLevel(
            attributeLevelPair.attributeId(),
            characterId,
            attributeLevelPair.newLevel());
    }

}
