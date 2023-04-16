package com.zpsm.rpgsessionassisstant.character;

import com.zpsm.rpgsessionassisstant.dto.AttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterAttributeDto;
import com.zpsm.rpgsessionassisstant.dto.CharacterDto1;
import com.zpsm.rpgsessionassisstant.dto.CreateCharacterDto;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import com.zpsm.rpgsessionassisstant.player.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.repository.*;
import com.zpsm.rpgsessionassisstant.room.RoomException;
import com.zpsm.rpgsessionassisstant.room.mapper.CharacterMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository mockCharacterRepository;
    @Mock
    private AttributeRepository mockAttributeRepository;
    @Mock
    private CharacterAttributeRepository mockCharacterAttributeRepository;
    @Mock
    private RoomRepository mockRoomRepository;
    @Mock
    private PlayerRepository mockPlayerRepository;
    @Mock
    private CharacterMapper mockCharacterMapper;
    @Mock
    private Principal mockPrincipal;
    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterService(
            mockCharacterRepository,
            mockAttributeRepository,
            mockCharacterAttributeRepository,
            mockRoomRepository,
            mockPlayerRepository,
            mockCharacterMapper);
    }

    @Test
    void givenValidIdShouldGetCharacterById() {
        // given
        long id = 1L;
        CharacterDto1 expected = new CharacterDto1(id, "Bezi", 3, 150, 2, 220, Set.of(), Set.of());
        Character character = new Character();
        character.setId(expected.id());
        character.setName(expected.name());
        character.setLevel(expected.level());
        character.setHealth(expected.health());
        character.setSkillPoints(expected.skillPoints());
        character.setExperience(expected.experience());
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(character));
        when(mockCharacterMapper.mapToOtherDTO(any())).thenReturn(expected);

        // when
        CharacterDto1 actual = characterService.getCharacterById(id);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdShouldThrowCharacterException() {
        // given
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(CharacterException.class, () -> characterService.getCharacterById(1L));
    }

    @Test
    void givenValidIdShouldGetPlayersCharacters() {
        // given
        CharacterDto1 dto = new CharacterDto1(1L, "Bezi", 3, 150, 2, 220, Set.of(), Set.of());
        Character character = new Character();
        character.setId(dto.id());
        character.setName(dto.name());
        character.setLevel(dto.level());
        character.setHealth(dto.health());
        character.setSkillPoints(dto.skillPoints());
        character.setExperience(dto.experience());
        List<CharacterDto1> expected = List.of(dto);
        List<Character> characters = List.of(character);
        when(mockCharacterRepository.findAllPlayersCharacters(anyLong())).thenReturn(characters);
        when(mockCharacterMapper.mapToOtherDTO(any())).thenReturn(dto);

        // when
        var actual = characterService.getPlayersCharacters(dto.id());

        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdShouldReturnEmptyCollection() {
        // given
        when(mockCharacterRepository.findAllPlayersCharacters(anyLong())).thenReturn(Collections.emptyList());

        // when
        var actual = characterService.getPlayersCharacters(1L);

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenCorrectDTOShouldCreateCharacter() {
        // given
        CreateCharacterDto dto = new CreateCharacterDto("Bezi", 2L, List.of("Strength"));
        Player player = new Player();
        Room room = new Room();
        Character savedWithPlayer = getCharacter();
        savedWithPlayer.setPlayer(player);
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Strength");
        CharacterAttribute characterAttribute = new CharacterAttribute();
        characterAttribute.setAttribute(attribute);
        characterAttribute.setCharacter(savedWithPlayer);
        characterAttribute.setAttributeLevel(1);
        Character savedWithAttributes = getCharacter();
        savedWithAttributes.setPlayer(player);
        savedWithAttributes.getCharacterAttributes().add(characterAttribute);
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(player));
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(mockCharacterRepository.save(any())).thenReturn(savedWithPlayer);
        when(mockAttributeRepository.findAllByNameIn(anyList())).thenReturn(List.of(attribute));
        when(mockCharacterAttributeRepository.saveAll(anySet())).thenReturn(List.of(characterAttribute));
        when(mockCharacterMapper.mapToOtherDTO(any())).thenReturn(getCharacterDto());

        // when
        CharacterDto1 actual = characterService.createCharacter(dto, mockPrincipal);

        // then
        assertEquals(getCharacterDto(), actual);
    }

    @Test
    void givenNonExistingPlayerShouldThrowPlayerNotFoundException() {
        // given
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(mockPrincipal.getName()).thenReturn("Testowy");

        // when // then
        assertThrows(PlayerNotFoundException.class, () ->
            characterService.createCharacter(
                new CreateCharacterDto("Bezi", 1L, List.of()),
                mockPrincipal));
    }

    @Test
    void givenNonExistingRoomShouldThrowRoomException() {
        // given
        CreateCharacterDto dto = new CreateCharacterDto("Bezi", 1L, List.of());
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(new Player()));
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(RoomException.class, () -> characterService.createCharacter(dto, mockPrincipal));
    }

    private Character getCharacter() {
        Character character = new Character();
        character.setId(1L);
        character.setName("Bezi");
        character.setLevel(2);
        character.setHealth(120);
        character.setSkillPoints(0);
        character.setExperience(100);
        return character;
    }

    private CharacterDto1 getCharacterDto() {
        return new CharacterDto1(
            1L,
            "Bezi",
            1,
            100,
            0,
            0,
            Set.of(),
            Set.of(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1)));
    }

}