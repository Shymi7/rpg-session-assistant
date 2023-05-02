package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.CreateItemAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CreateItemDto;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Item;
import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import com.zpsm.rpgsessionassisstant.repository.ItemRepository;
import com.zpsm.rpgsessionassisstant.util.ItemMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final AttributeService attributeService;

    public ItemDto getById(Long id) {
        return itemRepository.findById(id)
            .map(itemMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Item with id {} doesn't exists", id);
                return new EntityNotFoundException(String.format("Item with id %d doesn't exists", id));
            });
    }

    public ItemDto getByName(String name) {
        return itemRepository.findByName(name)
            .map(itemMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Item with name {} doesn't exists", name);
                return new EntityNotFoundException(String.format("Item with name %s doesn't exists", name));
            });
    }

    public Page<ItemDto> getPage(Pageable pageable) {
        return itemRepository.findAll(pageable)
            .map(itemMapper::mapToDto);
    }

    public ItemDto create(CreateItemDto createItemDto) {
        Item item = new Item();
        item.setName(createItemDto.name());
        item.setDescription(createItemDto.description());
        Item saved = itemRepository.save(item);
        addItemAttributesToItem(saved, createItemDto.itemAttributes());
        return itemMapper.mapToDto(itemRepository.save(saved));
    }

    public Character addItemToCharacter(Character character, long itemId) {
        if (null == character) {
            log.error("Character was null");
            throw new EntityNotFoundException("Character was null");
        }
        character.addItem(getItem(itemId));
        return character;
    }

    public Character removeItemFromCharacter(Character character, long itemId) {
        if (null == character) {
            log.error("Character was null");
            throw new EntityNotFoundException("Character was null");
        }
        character.removeItem(getItem(itemId));
        return character;
    }

    private void addItemAttributesToItem(Item item, Set<CreateItemAttributeDto> itemAttributeDtos) {
        itemAttributeDtos.forEach(itemAttributeDto -> {
            ItemAttribute newItemAttribute = attributeService.createNewItemAttribute(
                item,
                itemAttributeDto.attribute().name(),
                itemAttributeDto.attributeValue());
            item.getItemAttributes().add(newItemAttribute);
        });
    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> {
                log.error("Item with id {} not found", itemId);
                return new EntityNotFoundException(String.format("Item with id %d not found", itemId));
            });
    }

}
