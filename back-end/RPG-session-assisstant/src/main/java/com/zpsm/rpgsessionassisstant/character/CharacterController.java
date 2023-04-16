package com.zpsm.rpgsessionassisstant.character;

import com.zpsm.rpgsessionassisstant.dto.CharacterDto;
import com.zpsm.rpgsessionassisstant.dto.CreateCharacterDto;
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

}
