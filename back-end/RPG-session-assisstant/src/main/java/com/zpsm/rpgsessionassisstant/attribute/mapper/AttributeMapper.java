package com.zpsm.rpgsessionassisstant.attribute.mapper;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.model.Attribute;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper {

    public AttributeDto mapToDto(Attribute attribute) {
        return new AttributeDto(attribute.getId(), attribute.getName());
    }

}
