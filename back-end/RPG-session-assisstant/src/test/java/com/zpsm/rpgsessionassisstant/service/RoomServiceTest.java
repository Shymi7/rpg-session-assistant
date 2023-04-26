package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.FullRoomException;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Room;
import com.zpsm.rpgsessionassisstant.repository.CharacterRepository;
import com.zpsm.rpgsessionassisstant.repository.GamemasterRepository;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import com.zpsm.rpgsessionassisstant.repository.RoomRepository;
import com.zpsm.rpgsessionassisstant.util.CharacterMapper;
import com.zpsm.rpgsessionassisstant.util.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private GamemasterRepository mockGamemasterRepository;
    @Mock
    private PlayerRepository mockPlayerRepository;
    @Mock
    private RoomRepository mockRoomRepository;
    @Mock
    private CharacterRepository mockCharacterRepository;
    @Mock
    private GamemasterService mockGamemasterService;
    @Mock
    private Principal mockPrincipal;
    @Mock
    private CharacterMapper mockCharacterMapper;
    @Mock
    private RoomMapper mockRoomMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomService(
            mockGamemasterRepository,
            mockPlayerRepository,
            mockRoomRepository,
            mockCharacterRepository,
            mockGamemasterService,
            mockRoomMapper,
            mockCharacterMapper,
            passwordEncoder);
    }

    @Test
    void givenExistingNameShouldReturnRoom() {
        // given
        String roomName = "Room";
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setId(1L);
        Character character = new Character();
        character.setId(33L);
        Room room = new Room();
        room.setId(1L);
        room.setName(roomName);
        room.setGamemaster(gamemaster);
        room.setCapacity(8);
        room.setCharacters(Set.of(character));
        RoomDto expected = new RoomDto(
            room.getId(),
            gamemaster.getId(),
            Set.of(character.getId()),
            room.getCapacity(),
            room.getName());
        when(mockRoomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        when(mockRoomMapper.mapToDto(any())).thenReturn(expected);

        // when
        RoomDto actual = roomService.findRoomByName(roomName);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdf"})
    @NullAndEmptySource
    void givenIncorrectRoomNameShouldThrowEntityNotFoundException(String roomName) {
        // given
        when(mockRoomRepository.findByName(nullable(String.class))).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> roomService.findRoomByName(roomName));
    }

    @Test
    void givenCorrectIdShouldReturnRoom() {
        // given
        long roomId = 1L;
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setId(1L);
        Character character = new Character();
        character.setId(33L);
        Room room = new Room();
        room.setId(roomId);
        room.setName("roomName");
        room.setGamemaster(gamemaster);
        room.setCapacity(8);
        room.setCharacters(Set.of(character));
        RoomDto expected = new RoomDto(
            room.getId(),
            gamemaster.getId(),
            Set.of(character.getId()),
            room.getCapacity(),
            room.getName());
        when(mockRoomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(mockRoomMapper.mapToDto(any())).thenReturn(expected);

        // when
        RoomDto actual = roomService.findRoomById(roomId);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenIdShouldReturnListOfCharacters() {
        // given
        long roomId = 3L;
        Character character = getCharacter();
        CharacterDto characterDto = new CharacterDto(
            character.getId(),
            character.getName(),
            character.getLevel(),
            character.getHealth(),
            character.getSkillPoints(),
            character.getExperience(),
            Set.of(),
            Set.of());
        List<CharacterDto> expected = List.of(characterDto);
        when(mockRoomRepository.findAllByRoomId(roomId)).thenReturn(List.of(character));
        when(mockCharacterMapper.mapToDto(any())).thenReturn(characterDto);

        // when
        List<CharacterDto> actual = roomService.getCharactersFromRoom(roomId);

        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenRoomDtoAndPrincipalShouldCreateNewRoom() {
        // given
        CreateRoomDto dto = new CreateRoomDto(6, "password", "room without name");
        Player player = new Player();
        player.setLogin("Testowy");
        player.setId(1L);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setId(1L);
        gamemaster.setPlayer(player);
        Room savedRoom = new Room();
        savedRoom.setId(1L);
        savedRoom.setPassword(passwordEncoder.encode(dto.password()));
        savedRoom.setCapacity(dto.capacity());
        savedRoom.setName(dto.name());
        savedRoom.setGamemaster(gamemaster);
        RoomDto expected = new RoomDto(savedRoom.getId(), 1L, Set.of(), dto.capacity(), dto.name());

        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(player));
        when(mockGamemasterService.createGamemaster(any())).thenReturn(gamemaster);
        when(mockRoomRepository.save(any())).thenReturn(savedRoom);
        when(mockRoomMapper.mapToDto(any())).thenReturn(expected);

        // when
        RoomDto actual = roomService.createRoom(dto, mockPrincipal);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingPlayerShouldThrowPlayerNotFoundException() {
        // given
        String login = "Testowy";
        CreateRoomDto dto = new CreateRoomDto(6, "password", "room without name");
        when(mockPrincipal.getName()).thenReturn(login);
        when(mockPlayerRepository.findByLogin(login)).thenReturn(Optional.empty());

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> roomService.createRoom(dto, mockPrincipal));
    }

    @Test
    void givenCorrectDTOShouldLetPlayerIntoRoom() {
        // given
        EnterRoomDto enterRoomDto = new EnterRoomDto(1L, "password", 1L);
        Room room = new Room();
        room.setCapacity(4);
        when(mockRoomRepository.getPasswordOfRoom(anyLong()))
            .thenReturn(Optional.of(passwordEncoder.encode(enterRoomDto.password())));
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(new Character()));

        // when // then
        roomService.enterRoom(enterRoomDto);
        verify(mockRoomRepository, times(1)).save(any());
        verify(mockCharacterRepository, times(1)).save(any());
    }

    @Test
    void givenIncorrectRoomPasswordShouldThrowAccessDeniedException() {
        // given
        EnterRoomDto enterRoomDto = new EnterRoomDto(1L, "password1", 1L);
        when(mockRoomRepository.getPasswordOfRoom(anyLong()))
            .thenReturn(Optional.of(passwordEncoder.encode("password")));

        // when //then
        assertThrows(AccessDeniedException.class, () -> roomService.enterRoom(enterRoomDto));
    }

    @Test
    void givenIncorrectRoomIdShouldThrowEntityNotFoundException() {
        // given
        EnterRoomDto enterRoomDto = new EnterRoomDto(1L, "password", 1L);
        when(mockRoomRepository.getPasswordOfRoom(anyLong()))
            .thenReturn(Optional.of(passwordEncoder.encode("password")));
        when(mockRoomRepository.findById(enterRoomDto.roomId())).thenReturn(Optional.empty());

        // when //then
        assertThrows(EntityNotFoundException.class, () -> roomService.enterRoom(enterRoomDto));
    }

    @Test
    void givenFullRoomShouldThrowFullRoomException() {
        // given
        EnterRoomDto enterRoomDto = new EnterRoomDto(1L, "password", 1L);
        Room room = new Room();
        room.setCapacity(1);
        room.setCharacters(Set.of(new Character()));
        when(mockRoomRepository.getPasswordOfRoom(anyLong()))
            .thenReturn(Optional.of(passwordEncoder.encode("password")));
        when(mockRoomRepository.findById(enterRoomDto.roomId())).thenReturn(Optional.of(room));

        // when //then
        assertThrows(FullRoomException.class, () -> roomService.enterRoom(enterRoomDto));
    }

    @Test
    void givenCorrectIdAndPrincipalShouldDeleteRoom() {
        // given
        long id = 1L;
        Room room = new Room();
        room.setId(id);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setRoom(room);
        Player player = new Player();
        player.setGamemasters(Set.of(gamemaster));
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(player));

        // when
        roomService.deleteRoom(id, mockPrincipal);

        // then
        verify(mockRoomRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void givenIncorrectIdForRoomDeletionShouldThrowEntityNotFoundException() {
        // given
        long id = 1L;
        when(mockRoomRepository.findById(id)).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> roomService.deleteRoom(id, mockPrincipal));
    }

    @Test
    void givenNonExistingPlayerForRoomDeletionShouldThrowPlayerNotFoundException() {
        // given
        long id = 1L;
        Room room = new Room();
        room.setId(id);
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockRoomRepository.findById(id)).thenReturn(Optional.of(room));
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> roomService.deleteRoom(id, mockPrincipal));
    }

    @Test
    void givenCorrectDTOAndPrincipalShouldChangeRoomPassword() {
        // given
        RoomPasswordChangeDto dto = new RoomPasswordChangeDto(1L, "password1");
        Room room = new Room();
        room.setId(1L);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setRoom(room);
        Player player = new Player();
        player.setGamemasters(Set.of(gamemaster));
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(player));

        // when
        roomService.changeRoomPassword(dto, mockPrincipal);

        // then
        verify(mockRoomRepository, times(1)).save(any());
    }

    @Test
    void givenCorrectDTOAndPrincipalShouldRenameRoom() {
        // given
        RoomNameChangeDto dto = new RoomNameChangeDto(1L, "Giga Room");
        Room room = new Room();
        room.setId(1L);
        Gamemaster gamemaster = new Gamemaster();
        gamemaster.setRoom(room);
        Player player = new Player();
        player.setGamemasters(Set.of(gamemaster));
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(player));

        // when
        roomService.changeRoomName(dto, mockPrincipal);

        // then
        verify(mockRoomRepository, times(1)).save(any());
    }

    private Character getCharacter() {
        Character character = new Character();
        character.setId(1L);
        character.setLevel(3);
        character.setName("KARR");
        character.setSkillPoints(11);
        character.setLevel(3);
        character.setExperience(230);
        character.setHealth(170);
        return character;
    }

}
