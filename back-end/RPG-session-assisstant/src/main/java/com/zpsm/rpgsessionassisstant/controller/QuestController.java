package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.CreateQuestDto;
import com.zpsm.rpgsessionassisstant.dto.QuestDto;
import com.zpsm.rpgsessionassisstant.service.QuestService;
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
@RequestMapping("api/quest")
@AllArgsConstructor
public class QuestController {

    private final QuestService questService;

    @GetMapping
    public ResponseEntity<QuestDto> findQuestByName(@RequestParam String name) {
        log.info("Getting quest with name {}", name);
        return ResponseEntity.ok(questService.findQuestByName(name));
    }

    @GetMapping("/{questId}")
    public ResponseEntity<QuestDto> findQuestByName(@PathVariable Long questId) {
        log.info("Getting quest with id {}", questId);
        return ResponseEntity.ok(questService.findQuestById(questId));
    }

    @GetMapping("/character/{characterId}")
    public ResponseEntity<Collection<QuestDto>> findCharactersQuests(@PathVariable Long characterId) {
        log.info("Getting quest of character with id {}", characterId);
        return ResponseEntity.ok(questService.findCharactersQuests(characterId));
    }

    @PostMapping("/create")
    public ResponseEntity<QuestDto> createQuest(@Valid @RequestBody CreateQuestDto createQuestDto, Principal principal) {
        log.info("Creating new quest");
        return new ResponseEntity<>(questService.createQuest(createQuestDto, principal), HttpStatus.CREATED);
    }

}
