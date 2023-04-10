package com.zpsm.rpgsessionassisstant.attribute;

import com.zpsm.rpgsessionassisstant.attribute.mapper.AttributeMapper;
import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CreateNewAttributeDto;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import com.zpsm.rpgsessionassisstant.repository.AttributeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    public AttributeDto getAttributeByName(String name) {
        return attributeRepository.findByName(name)
            .map(attributeMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Attribute with name {} doesn't exits", name);
                return new AttributeException(String.format("Attribute with name %s doesn't exits", name));
            });
    }

    public Collection<AttributeDto> getAllAttributes() {
        return attributeRepository.findAll()
            .stream()
            .map(attributeMapper::mapToDto)
            .toList();
    }

    public AttributeDto getAttributeById(Long id) {
        return attributeRepository.findById(id)
            .map(attributeMapper::mapToDto)
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
        return attributeMapper.mapToDto(saved);
    }

}
