package com.zpsm.rpgsessionassisstant.item.mapper;

import com.zpsm.rpgsessionassisstant.attribute.mapper.AttributeMapper;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.model.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemMapper {

    private final AttributeMapper attributeMapper;

    public ItemDto mapToDto(Item item) {
        return new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getItemAttributes()
                .stream()
                .map(attributeMapper::mapToItemAttributeDto)
                .collect(Collectors.toSet()));
    }

}
