package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.CreateQuestDto;
import com.zpsm.rpgsessionassisstant.dto.QuestDto;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.NotGamemasterException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Gamemaster;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Quest;
import com.zpsm.rpgsessionassisstant.repository.QuestRepository;
import com.zpsm.rpgsessionassisstant.util.QuestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {

    @Mock
    private QuestRepository mockQuestRepository;
    @Mock
    private PlayerDetailsService mockDetailsService;
    @Mock
    private QuestMapper mockQuestMapper;
    @Mock
    private Principal mockPrincipal;
    private QuestService questService;

    @BeforeEach
    void setUp() {
        questService = new QuestService(
            mockQuestRepository,
            mockDetailsService,
            mockQuestMapper);
    }

    @Test
    void givenExistingNameShouldReturnQuestDto() {
        // given
        Quest quest = new Quest();
        quest.setId(1L);
        quest.setName("test");
        quest.setDescription("testowy");
        QuestDto expected = new QuestDto(quest.getId(), quest.getName(), quest.getDescription());
        when(mockQuestRepository.findByName(anyString())).thenReturn(Optional.of(quest));
        when(mockQuestMapper.mapToDto(any())).thenReturn(expected);

        // when
        QuestDto actual = questService.findQuestByName(quest.getName());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingNameShouldThrowEntityNotFoundException() {
        // given
        when(mockQuestRepository.findByName(anyString())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> questService.findQuestByName("asdf"));
    }

    @Test
    void givenExistingIdShouldReturnQuestDto() {
        // given
        Quest quest = new Quest();
        quest.setId(1L);
        quest.setName("test");
        quest.setDescription("testowy");
        QuestDto expected = new QuestDto(quest.getId(), quest.getName(), quest.getDescription());
        when(mockQuestRepository.findById(anyLong())).thenReturn(Optional.of(quest));
        when(mockQuestMapper.mapToDto(any())).thenReturn(expected);

        // when
        QuestDto actual = questService.findQuestById(quest.getId());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistingIdShouldThrowEntityNotFoundException() {
        // given
        when(mockQuestRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when // then
        assertThrows(EntityNotFoundException.class, () -> questService.findQuestById(-1L));
    }

    @Test
    void givenExistingCharacterShouldReturnItsQuests() {
        // given
        Quest quest = new Quest();
        quest.setId(1L);
        quest.setName("Test");
        quest.setDescription("testowy opis");
        QuestDto expected = new QuestDto(quest.getId(), quest.getName(), quest.getDescription());
        when(mockQuestRepository.findByCharactersId(anyLong())).thenReturn(Set.of(quest));
        when(mockQuestMapper.mapToDto(any())).thenReturn(expected);

        // when
        Collection<QuestDto> actual = questService.findCharactersQuests(quest.getId());

        // then
        assertIterableEquals(Set.of(expected), actual);
    }

    @Test
    void givenCharacterWithNoQuestsShouldReturnEmptyCollection() {
        // given
        when(mockQuestRepository.findByCharactersId(anyLong())).thenReturn(Set.of());

        // when
        Collection<QuestDto> actual = questService.findCharactersQuests(1L);

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenCorrectDtoAndPlayerWhoIsGamemasterShouldCreateQuest() {
        Player player = new Player();
        player.getGamemasters().add(new Gamemaster());
        Quest quest = new Quest();
        quest.setId(1L);
        quest.setName("Test");
        quest.setDescription("Testowy quest");
        QuestDto expected = new QuestDto(quest.getId(), quest.getName(), quest.getDescription());
        CreateQuestDto dto = new CreateQuestDto(quest.getName(), quest.getDescription());
        when(mockPrincipal.getName()).thenReturn("Testowy");
        when(mockDetailsService.loadUserByUsername(anyString())).thenReturn(player);
        when(mockQuestRepository.save(any())).thenReturn(quest);
        when(mockQuestMapper.mapToDto(any())).thenReturn(expected);

        // when
        QuestDto actual = questService.createQuest(dto, mockPrincipal);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenPlayerWhoIsNotGamemasterShouldThrowNotGamemasterException() {
        // given
        CreateQuestDto dto = new CreateQuestDto("Test", "Testowy");
        when(mockDetailsService.loadUserByUsername(anyString())).thenReturn(new Player());
        when(mockPrincipal.getName()).thenReturn("Testowy");

        // when // then
        assertThrows(NotGamemasterException.class, () -> questService.createQuest(dto, mockPrincipal));
    }

    @Test
    void givenCharacterAndQuestIdShouldAddQuest() {
        // given
        Quest quest = new Quest();
        quest.setId(1L);
        when(mockQuestRepository.findById(anyLong())).thenReturn(Optional.of(quest));

        // when
        Character actual = questService.addQuestToCharacter(new Character(), quest.getId());

        // then
        assertFalse(actual.getQuests().isEmpty());
    }

    @Test
    void givenCharacterAndQuestIdShouldRemoveQuest() {
        // given
        Quest quest = new Quest();
        quest.setId(1L);
        Character character = new Character();
        character.getQuests().add(quest);
        when(mockQuestRepository.findById(anyLong())).thenReturn(Optional.of(quest));

        // when
        Character actual = questService.removeQuestFromCharacter(character, quest.getId());

        // then
        assertTrue(actual.getQuests().isEmpty());
    }

}
