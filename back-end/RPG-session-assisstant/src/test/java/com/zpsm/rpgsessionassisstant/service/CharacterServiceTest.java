package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.*;
import com.zpsm.rpgsessionassisstant.exception.CharacterNotInAnyRoomException;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.ModifyingAttributesException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.*;
import com.zpsm.rpgsessionassisstant.repository.CharacterRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository mockCharacterRepository;
    @Mock
    private CharacterMapper mockCharacterMapper;
    @Mock
    private ItemService mockItemService;
    @Mock
    private PlayerDetailsService mockDetailsService;
    @Mock
    private QuestService mockQuestService;
    @Mock
    private AttributeService mockAttributeService;
    @Mock
    private Principal mockPrincipal;
    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterService(
            mockCharacterRepository,
            mockCharacterMapper,
            mockDetailsService,
            mockItemService,
            mockQuestService,
            mockAttributeService);
    }

    @Test
    void givenValidIdShouldGetCharacterById() {
        // given
        long id = 1L;
        CharacterDto expected = new CharacterDto(
            id,
            "Bezi",
            3,
            150,
            2,
            220,
            "Test character",
            Set.of(),
            Set.of(),
            Set.of());
        Character character = new Character();
        character.setId(expected.id());
        character.setName(expected.name());
        character.setLevel(expected.level());
        character.setHealth(expected.health());
        character.setSkillPoints(expected.skillPoints());
        character.setExperience(expected.experience());
        character.setDescription(expected.description());
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
        CharacterDto dto = new CharacterDto(
            1L,
            "Bezi",
            3,
            150,
            2,
            220,
            "Test character",
            Set.of(),
            Set.of(),
            Set.of());
        Character character = new Character();
        character.setId(dto.id());
        character.setName(dto.name());
        character.setLevel(dto.level());
        character.setHealth(dto.health());
        character.setSkillPoints(dto.skillPoints());
        character.setExperience(dto.experience());
        character.setDescription(dto.description());
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
        CreateCharacterDto dto = new CreateCharacterDto("Bezi", "Test character", List.of("Strength"));
        Player player = new Player();
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
        when(mockDetailsService.loadUserByUsername(anyString())).thenReturn(player);
        when(mockCharacterRepository.save(any())).thenReturn(savedWithPlayer);
        when(mockAttributeService.saveCharacterAttributes(any(), anyList())).thenReturn(Set.of(characterAttribute));
        when(mockCharacterMapper.mapToDto(any())).thenReturn(getCharacterDto());

        // when
        CharacterDto actual = characterService.createCharacter(dto, mockPrincipal);

        // then
        assertEquals(getCharacterDto(), actual);
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
            character.getDescription(),
            Set.of(new ItemDto(item.getId(), item.getName(), item.getDescription(), Set.of())),
            Set.of(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1)),
            Set.of());
        Character characterWithItem = getCharacter();
        characterWithItem.getItems().add(item);
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(getCharacter()));
        when(mockItemService.addItemToCharacter(any(), anyLong())).thenReturn(characterWithItem);
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
        when(mockItemService.removeItemFromCharacter(any(), anyLong())).thenReturn(getCharacter());
        when(mockCharacterMapper.mapToDto(any())).thenReturn(expected);

        // when
        CharacterDto actual = characterService.removeItem(dto);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void givenCharacterThatIsInRoomShouldAddQuest() {
        // given
        Room room = new Room();
        Quest quest = new Quest();
        quest.setId(1L);
        Character character = getCharacter();
        character.getRooms().add(room);
        Character characterWithQuest = getCharacter();
        characterWithQuest.getRooms().add(room);
        characterWithQuest.getQuests().add(quest);
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(character));
        when(mockQuestService.addQuestToCharacter(any(), anyLong())).thenReturn(characterWithQuest);

        // when
        characterService.addQuest(new AddOrRemoveFromCharacterDto(character.getId(), quest.getId()));

        // then
        verify(mockCharacterRepository, atMostOnce()).save(characterWithQuest);
    }

    @Test
    void givenCharacterThatIsNotInAnyRoomShouldThrowCharacterNotInAnyRoomException() {
        // given
        Character character = getCharacter();
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(character));

        // when // then
        assertThrows(CharacterNotInAnyRoomException.class,
            () -> characterService.addQuest(new AddOrRemoveFromCharacterDto(character.getId(), 1L)));
    }

    @Test
    void givenCharacterThatIsInRoomShouldRemoveQuest() {
        // given
        Room room = new Room();
        Quest quest = new Quest();
        quest.setId(1L);
        Character characterWithoutQuest = getCharacter();
        characterWithoutQuest.getRooms().add(room);
        Character characterWithQuest = getCharacter();
        characterWithQuest.getRooms().add(room);
        characterWithQuest.getQuests().add(quest);
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(characterWithQuest));
        when(mockQuestService.removeQuestFromCharacter(any(), anyLong())).thenReturn(characterWithoutQuest);

        // when
        characterService.removeQuest(new AddOrRemoveFromCharacterDto(characterWithoutQuest.getId(), quest.getId()));

        // then
        verify(mockCharacterRepository, atMostOnce()).save(characterWithoutQuest);
    }

    @Test
    void givenCharacterThatIsNotInAnyRoomWhenRemovingQuestShouldThrowCharacterNotInAnyRoomException() {
        // given
        Character character = getCharacter();
        when(mockCharacterRepository.findById(anyLong())).thenReturn(Optional.of(character));

        // when // then
        assertThrows(CharacterNotInAnyRoomException.class,
            () -> characterService.removeQuest(new AddOrRemoveFromCharacterDto(character.getId(), 1L)));
    }

    @Test
    void givenExitingCharacterIdAndCorrectDtoShouldModifyCharactersAttribute() {
        // given
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Strength");

        CharacterAttribute characterAttribute = new CharacterAttribute();
        characterAttribute.setAttribute(attribute);
        characterAttribute.setAttributeLevel(0);

        Character character = getCharacter();
        character.setSkillPoints(2);
        characterAttribute.setCharacter(character);
        character.getCharacterAttributes().add(characterAttribute);

        var dto = new ModifyCharactersAttributesDto(attribute.getId(), 1);
        CharacterDto expected = getCharacterDto();
        when(mockCharacterRepository.findById(any())).thenReturn(Optional.of(character));
        when(mockCharacterRepository.doesCharacterHasGivenAttributes(anyLong(), anyLong())).thenReturn(Optional.of(character));
        when(mockCharacterRepository.save(any())).thenReturn(getCharacter());
        when(mockCharacterMapper.mapToDto(any())).thenReturn(expected);

        // when
        CharacterDto actual = characterService.modifyCharactersAttribute(character.getId(), dto);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenCharacterThatDoesntHaveGivenAttributeShouldThrowModifyingAttributesException() {
        // given
        var dto = new ModifyCharactersAttributesDto(1L, 1);
        when(mockCharacterRepository.findById(any())).thenReturn(Optional.of(getCharacter()));
        when(mockCharacterRepository.doesCharacterHasGivenAttributes(1L, dto.attributeId()))
            .thenReturn(Optional.empty());

        // when // then
        assertThrows(ModifyingAttributesException.class,
            () -> characterService.modifyCharactersAttribute(1L, dto));
    }

    @Test
    void givenCharacterWithNotEnoughSkillPointsShouldThrowModifyingAttributesException() {
        Character character = getCharacter();
        var dto = new ModifyCharactersAttributesDto(1L, 1);
        when(mockCharacterRepository.findById(any())).thenReturn(Optional.of(character));
        when(mockCharacterRepository.doesCharacterHasGivenAttributes(1L, dto.attributeId()))
            .thenReturn(Optional.of(character));

        // when // then
        assertThrows(ModifyingAttributesException.class,
            () -> characterService.modifyCharactersAttribute(1L, dto));
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
            "Test character",
            Set.of(),
            Set.of(new CharacterAttributeDto(new AttributeDto(1L, "Strength"), 1)),
            Set.of());
    }

}
