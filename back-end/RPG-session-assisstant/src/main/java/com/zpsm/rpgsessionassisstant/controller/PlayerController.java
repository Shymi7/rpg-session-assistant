package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.PlayerDto;
import com.zpsm.rpgsessionassisstant.service.PlayerDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/player")
@AllArgsConstructor
public class PlayerController {

    private final PlayerDetailsService playerDetailsService;

    @GetMapping
    public ResponseEntity<PlayerDto> findPlayerByName(@RequestParam String login) {
        log.info("Getting player with name {}", login);
        return ResponseEntity.ok(playerDetailsService.getPlayerByLogin(login));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> findPlayerById(@PathVariable Long id) {
        log.info("Getting player with id {}", id);
        return ResponseEntity.ok(playerDetailsService.getPlayerById(id));
    }

}
