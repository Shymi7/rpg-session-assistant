package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.PlayerDto;
import com.zpsm.rpgsessionassisstant.dto.RoomDto;
import com.zpsm.rpgsessionassisstant.service.PlayerDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{playerId}/player-in-rooms")
    public ResponseEntity<List<RoomDto>> findRoomsWhichPlayersCharactersBelongTo(@PathVariable Long playerId) {
        log.info("Getting rooms for player with id {}", playerId);
        return ResponseEntity.ok(playerDetailsService.findRoomsWhichPlayersCharactersBelongTo(playerId));
    }

    @GetMapping("/{playerId}/gamemaster-in-rooms")
    public ResponseEntity<List<RoomDto>> findRoomsWhichPlayerIsGamemasterIn(@PathVariable Long playerId) {
        log.info("Getting rooms for player with id {}", playerId);
        return ResponseEntity.ok(playerDetailsService.findRoomsWhichPlayerIsGamemasterIn(playerId));
    }

}
