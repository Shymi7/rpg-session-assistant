package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import com.zpsm.rpgsessionassisstant.repository.*;
import com.zpsm.rpgsessionassisstant.util.CharacterMapper;
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
    private ItemService mockItemService;
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
            mockCharacterMapper,
            mockItemService);
    }

    @Test
    void givenValidIdShouldGetCharacterById() {
        // given
        long id = 1L;
        CharacterDto expected = new CharacterDto(id, "Bezi", 3, 150, 2, 220, Set.of(), Set.of());
        Character character = new Character();
        character.setId(expected.id());
        character.setName(expected.name());
        character.setLevel(expected.level());
        character.setHealth(expected.health());
        character.setSkillPoints(expected.skillPoints());
        character.setExperience(expected.experience());
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(character));
        when(mockCharacterMapper.mapToDto(any())).thenReturn(expected);

        // when
        CharacterDto actual = characterService.getCharacterById(id);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdShouldThrowEntityNotFoundException() {
        // given
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> characterService.getCharacterById(1L));
    }

    @Test
    void givenValidIdShouldGetPlayersCharacters() {
        // given
        CharacterDto dto = new CharacterDto(1L, "Bezi", 3, 150, 2, 220, Set.of(), Set.of());
        Character character = new Character();
        character.setId(dto.id());
        character.setName(dto.name());
        character.setLevel(dto.level());
        character.setHealth(dto.health());
        character.setSkillPoints(dto.skillPoints());
        character.setExperience(dto.experience());
        List<CharacterDto> expected = List.of(dto);
        List<Character> characters = List.of(character);
        when(mockCharacterRepository.findAllPlayersCharacters(anyLong())).thenReturn(characters);
        when(mockCharacterMapper.mapToDto(any())).thenReturn(dto);

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
        when(mockCharacterMapper.mapToDto(any())).thenReturn(getCharacterDto());

        // when
        CharacterDto actual = characterService.createCharacter(dto, mockPrincipal);

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
    void givenNonExistingRoomShouldThrowEntityNotFoundException() {
        // given
        CreateCharacterDto dto = new CreateCharacterDto("Bezi", 1L, List.of());
        when(mockPlayerRepository.findByLogin(anyString())).thenReturn(Optional.of(new Player()));
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> characterService.createCharacter(dto, mockPrincipal));
    }

    @Test
    void givenCorrectDtoShouldAddItemToCharacter() {
        // given
        Character character = getCharacter();
        Item item = new Item();
        item.setId(1L);
        item.setName("Stick");
        item.setDescription("Stick of truth");
        AddOrRemoveFromCharacterDto dto = new AddOrRemoveFromCharacterDto(1L, 1L);
        CharacterDto expected = new CharacterDto(character.getId(),
            character.getName(),
            character.getLevel(),
            character.getHealth(),
            character.getSkillPoints(),
            character.getExperience(),
            Set.of(new ItemDto(item.getId(), item.getName(), item.getDescription(), Set.of())),
            Set.of(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1)));
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(getCharacter()));
        when(mockItemService.getItem(anyLong())).thenReturn(item);
        when(mockCharacterMapper.mapToDto(any())).thenReturn(expected);

        // when
        CharacterDto actual = characterService.addItem(dto);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenCorrectDtoShouldRemoveItemFromCharacter() {
        // given
        Character character = getCharacter();
        Item item = new Item();
        item.setId(1L);
        item.setName("Stick");
        item.setDescription("Stick of truth");
        character.getItems().add(item);
        CharacterDto expected = getCharacterDto();
        AddOrRemoveFromCharacterDto dto = new AddOrRemoveFromCharacterDto(1L, 1L);
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(character));
        when(mockItemService.getItem(anyLong())).thenReturn(item);
        when(mockCharacterMapper.mapToDto(any())).thenReturn(expected);

        // when
        CharacterDto actual = characterService.removeItem(dto);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void givenExistingIdShouldReturnCharacterEntity() {
        // given
        Character expected = getCharacter();
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        // when
        Character actual = characterService.getCharacter(1L);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdForEntityShouldThrowEntityNotFoundException() {
        // given
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> characterService.getCharacter(1L));
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

    private CharacterDto getCharacterDto() {
        return new CharacterDto(
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
