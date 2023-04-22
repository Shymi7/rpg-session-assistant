package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.CreateItemDto;
import com.zpsm.rpgsessionassisstant.dto.ItemDto;
import com.zpsm.rpgsessionassisstant.service.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ItemDto> getByName(@RequestParam String name) {
        log.info("Getting item with name {}", name);
        return ResponseEntity.ok(itemService.getByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long id) {
        log.info("Getting item with id {}", id);
        return ResponseEntity.ok(itemService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ItemDto> create(@Valid @RequestBody CreateItemDto createItemDto) {
        log.info("Creating new item");
        return new ResponseEntity<>(itemService.create(createItemDto), HttpStatus.CREATED);
    }

}
