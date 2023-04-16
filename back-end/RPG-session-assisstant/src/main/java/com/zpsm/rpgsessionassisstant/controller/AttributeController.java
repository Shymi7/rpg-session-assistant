package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CreateNewAttributeDto;
import com.zpsm.rpgsessionassisstant.service.AttributeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api/attribute")
@AllArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping
    public ResponseEntity<AttributeDto> getAttributeById(@RequestParam String name) {
        log.info("Getting attribute with name {}", name);
        return ResponseEntity.ok(attributeService.getAttributeByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<AttributeDto>> getAllAttributes() {
        log.info("Getting all attributes");
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDto> getAttributeById(@PathVariable Long id) {
        log.info("Getting attribute with id {}", id);
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<AttributeDto> createNewAttribute(@Valid @RequestBody CreateNewAttributeDto dto) {
        log.info("Creating new attribute");
        return new ResponseEntity<>(attributeService.createNewAttribute(dto), HttpStatus.CREATED);
    }

}
