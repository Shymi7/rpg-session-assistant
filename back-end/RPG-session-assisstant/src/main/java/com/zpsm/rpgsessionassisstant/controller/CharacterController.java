package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.service.CharacterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("api/character")
@AllArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable Long id) {
        log.info("Getting character with id {}", id);
        return ResponseEntity.ok(characterService.getCharacterById(id));
    }

    @GetMapping("/player-characters/{id}")
    public ResponseEntity<Collection<CharacterDto>> getPlayersCharacter(@PathVariable Long id) {
        log.info("Getting characters of player with id {}", id);
        return ResponseEntity.ok(characterService.getPlayersCharacters(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CharacterDto> createCharacter(@Valid @RequestBody CreateCharacterDto dto, Principal principal) {
        log.info("Creating new character");
        return new ResponseEntity<>(characterService.createCharacter(dto, principal), HttpStatus.CREATED);
    }

    @PatchMapping("/add-item")
    public ResponseEntity<CharacterDto> addItemToCharacter(@Valid @RequestBody AddOrRemoveFromCharacterDto dto) {
        log.info("Add item to character with id {}", dto.characterId());
        return ResponseEntity.ok(characterService.addItem(dto));
    }

    @PatchMapping("/remove-item")
    public ResponseEntity<CharacterDto> removeItemFromCharacter(@Valid @RequestBody AddOrRemoveFromCharacterDto dto) {
        log.info("Removing item from character with id {}", dto.characterId());
        return ResponseEntity.ok(characterService.removeItem(dto));
    }

    @PatchMapping("/add-quest")
    public ResponseEntity<Void> addQuestToCharacter(@Valid @RequestBody AddOrRemoveFromCharacterDto dto) {
        log.info("Add quest to character with id {}", dto.characterId());
        characterService.addQuest(dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/remove-quest")
    public ResponseEntity<Void> removeQuestFromCharacter(@Valid @RequestBody AddOrRemoveFromCharacterDto dto) {
        log.info("Remove quest from character with id {}", dto.characterId());
        characterService.removeQuest(dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/modify-attributes")
    public ResponseEntity<CharacterDto> modifyCharactersAttribute(
        @PathVariable Long id,
        @Valid @RequestBody ModifyCharactersAttributesDto dto) {

        log.info("Changing attributes of character with id {}", id);
        return new ResponseEntity<>(characterService.modifyCharactersAttribute(id, dto), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/add-xp")
    public ResponseEntity<Void> addExperienceToCharacter(
        @Valid @RequestBody AddXpDto dto,
        Principal principal) {

        log.info("Add experience to character with id {}", dto.characterId());
        characterService.addExperienceToCharacter(dto, principal);
        return ResponseEntity.noContent().build();
    }

}
