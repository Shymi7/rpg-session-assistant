package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.FullRoomException;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Room;
import com.zpsm.rpgsessionassisstant.repository.CharacterRepository;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import com.zpsm.rpgsessionassisstant.repository.RoomRepository;
import com.zpsm.rpgsessionassisstant.util.CharacterMapper;
import com.zpsm.rpgsessionassisstant.util.RoomMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RoomService {

    private final GamemasterRepository gamemasterRepository;
    private final RoomRepository roomRepository;
    private final CharacterRepository characterRepository;
    private final GamemasterService gamemasterService;
    private final PlayerDetailsService playerDetailsService;
    private final RoomMapper roomMapper;
    private final CharacterMapper characterMapper;
    private final PasswordEncoder passwordEncoder;

    public RoomDto findRoomByName(String name) {
        return roomRepository.findByName(name)
            .map(roomMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Room with name {} doesn't exist", name);
                return new EntityNotFoundException(String.format("Room with name %s doesn't exist", name));
            });
    }

    public Page<RoomDto> getPage(Pageable pageable) {
        return roomRepository.findAll(pageable)
            .map(roomMapper::mapToDto);
    }

    public RoomDto findRoomById(Long id) {
        return roomMapper.mapToDto(getRoomById(id));
    }

    public List<CharacterDto> getCharactersFromRoom(long roomId) {
        return roomRepository.findAllByRoomId(roomId)
            .stream()
            .map(characterMapper::mapToDto)
            .toList();
    }

    @Transactional
    public RoomDto createRoom(CreateRoomDto dto, Principal principal) {
        Player player = (Player) playerDetailsService.loadUserByUsername(principal.getName());
        Gamemaster savedGamemaster = gamemasterService.createGamemaster(player);
        Room room = newRoomEntity(dto, savedGamemaster);
        Room savedRoom = roomRepository.save(room);
        savedGamemaster.setRoom(savedRoom);
        gamemasterRepository.save(savedGamemaster);
        log.info("Room '{}' created", dto.name());
        return roomMapper.mapToDto(savedRoom);
    }

    public void enterRoom(EnterRoomDto dto) {
        if (!isPasswordCorrect(dto.roomName(), dto.password())) {
            throw new AccessDeniedException("Incorrect password to room");
        }
        Room room = getRoomByName(dto.roomName());
        if (room.getCharacters().size() >= room.getCapacity()) {
            log.error("Room is full");
            throw new FullRoomException("Room is full");
        }
        addCharacterToRoom(dto.characterId(), room);
        log.info("Player entered the room");
    }

    public void deleteRoom(long id, Principal principal) {
        Room room = getRoomById(id);
        gamemasterService.findGamemasterForGivenRoomAndDoSomething(
            room.getId(),
            principal.getName(),
            gamemaster -> roomRepository.deleteById(id));
        log.info("Room deleted");
    }

    public void changeRoomPassword(RoomPasswordChangeDto dto, Principal principal) {
        Room room = getRoomById(dto.roomId());
        gamemasterService.findGamemasterForGivenRoomAndDoSomething(room.getId(), principal.getName(), gamemaster -> {
            room.setPassword(passwordEncoder.encode(dto.newPassword()));
            roomRepository.save(room);
        });
        log.info("Room password changed");
    }

    public void changeRoomName(RoomNameChangeDto dto, Principal principal) {
        Room room = getRoomById(dto.roomId());
        gamemasterService.findGamemasterForGivenRoomAndDoSomething(room.getId(), principal.getName(), gamemaster -> {
            room.setName(passwordEncoder.encode(dto.newRoomName()));
            roomRepository.save(room);
        });
        log.info("Room name changed");
    }

    public CharacterDto findCharacterOfLoggedInPlayerFromGivenRoom(Long roomId, Principal principal) {
        return roomRepository.findPlayerCharacterFromGivenRoom(principal.getName(), roomId)
            .map(characterMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Room or character doesn't exist");
                return new EntityNotFoundException("Room or character doesn't exist");
            });
    }

    private Room newRoomEntity(CreateRoomDto dto, Gamemaster gamemaster) {
        Room newRoom = new Room();
        newRoom.setName(dto.name());
        newRoom.setCapacity(dto.capacity());
        newRoom.setPassword(passwordEncoder.encode(dto.password()));
        newRoom.setGamemaster(gamemaster);
        return newRoom;
    }

    private boolean isPasswordCorrect(String roomName, String rawPassword) {
        String encodedPassword = roomRepository.getPasswordOfRoom(roomName)
            .orElseThrow();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void addCharacterToRoom(long characterId, Room room) {
        characterRepository.findById(characterId)
            .ifPresent(character -> {
                room.getCharacters().add(character);
                Room savedRoom = roomRepository.save(room);
                character.getRooms().add(savedRoom);
                characterRepository.save(character);
            });
    }

    private Room getRoomById(long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Room {} doesn't exist", id);
                return new EntityNotFoundException(String.format("Room %d doesn't exist", id));
            });
    }

    private Room getRoomByName(String roomName) {
        return roomRepository.findByName(roomName)
            .orElseThrow(() -> {
                log.error("Room {} doesn't exist", roomName);
                return new EntityNotFoundException(String.format("Room %s doesn't exist", roomName));
            });
    }

}
