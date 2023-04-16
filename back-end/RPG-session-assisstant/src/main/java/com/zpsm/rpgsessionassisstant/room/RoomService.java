package com.zpsm.rpgsessionassisstant.room;

import com.zpsm.rpgsessionassisstant.character.CharacterMapper;
import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Room;
import com.zpsm.rpgsessionassisstant.player.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.repository.CharacterRepository;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import com.zpsm.rpgsessionassisstant.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
@AllArgsConstructor
public class RoomService {

    private final GamemasterRepository gamemasterRepository;
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;
    private final CharacterRepository characterRepository;
    private final GamemasterService gamemasterService;
    private final RoomMapper roomMapper;
    private final CharacterMapper characterMapper;
    private final PasswordEncoder passwordEncoder;

    public RoomDto findRoomByName(String name) {
        return roomRepository.findByName(name)
                .map(roomMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error("Room with name {} doesn't exist", name);
                    return new RoomException(String.format("Room with name %s doesn't exist", name));
                });
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

    public RoomDto createRoom(CreateRoomDto dto, Principal principal) {
        Player player = playerRepository.findByLogin(principal.getName())
            .orElseThrow(() -> new PlayerNotFoundException(
                String.format("Player with login %s not found", principal.getName())));
        Gamemaster savedGamemaster = gamemasterService.createGamemaster(player);
        Room room = newRoomEntity(dto, savedGamemaster);
        Room savedRoom = roomRepository.save(room);
        savedGamemaster.setRoom(savedRoom);
        gamemasterRepository.save(savedGamemaster);
        log.info("Room '{}' created", dto.name());
        return roomMapper.mapToDto(savedRoom);
    }

    public void enterRoom(EnterRoomDto dto) {
        if (!isPasswordCorrect(dto.roomId(), dto.password())) {
            throw new AccessDeniedException("Incorrect password to room");
        }
        Room room = getRoomById(dto.roomId());
        if (room.getCharacter().size() >= room.getCapacity()) {
            log.error("Room is full");
            throw new RoomException("Room is full");
        }
        addCharacterToRoom(dto.characterId(), room);
        log.info("Player entered the room");
    }

    public void deleteRoom(long id, Principal principal) {
        Room room = getRoomById(id);
        findGamemasterForGivenRoomAndDoSomething(
            room.getId(),
            principal.getName(),
            gamemaster -> roomRepository.deleteById(id));
        log.info("Room deleted");
    }

    public void changeRoomPassword(RoomPasswordChangeDto dto, Principal principal) {
        Room room = getRoomById(dto.roomId());
        findGamemasterForGivenRoomAndDoSomething(
            room.getId(),
            principal.getName(),
            gamemaster -> {
                room.setPassword(passwordEncoder.encode(dto.newPassword()));
                roomRepository.save(room);
            });
        log.info("Room password changed");
    }

    public void changeRoomName(RoomNameChangeDto dto, Principal principal) {
        Room room = getRoomById(dto.roomId());
        findGamemasterForGivenRoomAndDoSomething(
            room.getId(),
            principal.getName(),
            gamemaster -> {
                room.setName(passwordEncoder.encode(dto.newRoomName()));
                roomRepository.save(room);
            });
        log.info("Room name changed");
    }

    private Room newRoomEntity(CreateRoomDto dto, Gamemaster gamemaster) {
        Room newRoom = new Room();
        newRoom.setName(dto.name());
        newRoom.setCapacity(dto.capacity());
        newRoom.setPassword(passwordEncoder.encode(dto.password()));
        newRoom.setGamemaster(gamemaster);
        return newRoom;
    }

    private boolean isPasswordCorrect(long roomId, String rawPassword) {
        String encodedPassword = roomRepository.getPasswordOfRoom(roomId)
            .orElseThrow();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void addCharacterToRoom(long characterId, Room room) {
        characterRepository.findById(characterId)
            .ifPresent(character -> {
                room.getCharacter().add(character);
                Room savedRoom = roomRepository.save(room);
                character.getRooms().add(savedRoom);
                characterRepository.save(character);
            });
    }

    private void findGamemasterForGivenRoomAndDoSomething(long roomId, String playerName, Consumer<Gamemaster> action) {
        Player player = playerRepository.findByLogin(playerName)
            .orElseThrow(() -> {
                log.error("Player with login {} not found", playerName);
                return new PlayerNotFoundException(
                    String.format("Player with login %s not found", playerName));
            });
        player.getGamemasters()
            .stream()
            .filter(gamemaster -> gamemaster.getRoom().getId() == roomId)
            .findFirst()
            .ifPresent(action);
    }

    private Room getRoomById(long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Room {} doesn't exist", id);
                return new RoomException(String.format("Room %d doesn't exist", id));
            });
    }

}
