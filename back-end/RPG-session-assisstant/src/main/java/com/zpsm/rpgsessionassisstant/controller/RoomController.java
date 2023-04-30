package com.zpsm.rpgsessionassisstant.controller;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.service.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/room")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<RoomDto> findRoomByName(@RequestParam String name) {
        log.info("Getting room with name {}", name);
        return ResponseEntity.ok(roomService.findRoomByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RoomDto>> getPage(Pageable pageable) {
        log.info("Getting page {}", pageable.getPageNumber());
        return ResponseEntity.ok(roomService.getPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> findRoomById(@PathVariable Long id) {
        log.info("Getting room with id {}", id);
        return ResponseEntity.ok(roomService.findRoomById(id));
    }

    @GetMapping("/{id}/characters")
    public ResponseEntity<Collection<CharacterDto>> characters(@PathVariable long id) {
        log.info("Getting characters from room {}", id);
        List<CharacterDto> charactersFromRoom = roomService.getCharactersFromRoom(id);
        return ResponseEntity.ok(charactersFromRoom);
    }

    @PostMapping("/enter-room")
    public ResponseEntity<Void> enterRoom(@Valid @RequestBody EnterRoomDto dto) {
        log.info("Entering room {}", dto.roomName());
        roomService.enterRoom(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-room")
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody CreateRoomDto dto, Principal principal) {
        log.info("Creating new room");
        RoomDto createdRoom = roomService.createRoom(dto, principal);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changeRoomPassword(
        @Valid @RequestBody RoomPasswordChangeDto dto,
        Principal principal) {

        log.info("Changing password for room: {}", dto.roomId());
        roomService.changeRoomPassword(dto, principal);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-name")
    public ResponseEntity<Void> changeRoomName(
        @Valid @RequestBody RoomNameChangeDto dto,
        Principal principal) {

        log.info("Changing password for room: {}", dto.roomId());
        roomService.changeRoomName(dto, principal);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-room/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable long id, Principal principal) {
        log.info("Deleting room {}", id);
        roomService.deleteRoom(id, principal);
        return ResponseEntity.ok().build();
    }

}
