package com.zpsm.rpgsessionassisstant.service;

import com.zpsm.rpgsessionassisstant.dto.CreateQuestDto;
import com.zpsm.rpgsessionassisstant.dto.QuestDto;
import com.zpsm.rpgsessionassisstant.exception.EntityNotFoundException;
import com.zpsm.rpgsessionassisstant.exception.NotGamemasterException;
import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.model.Quest;
import com.zpsm.rpgsessionassisstant.repository.QuestRepository;
import com.zpsm.rpgsessionassisstant.util.QuestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final CharacterService characterService;
    private final PlayerDetailsService playerDetailsService;
    private final QuestMapper questMapper;

    public QuestDto findQuestByName(String name) {
        return questRepository.findByName(name)
            .map(questMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Quest with name {} doesn't exists", name);
                return new EntityNotFoundException(String.format("Quest with name %s doesn't exists", name));
            });
    }

    public QuestDto findQuestById(Long id) {
        return questRepository.findById(id)
            .map(questMapper::mapToDto)
            .orElseThrow(() -> {
                log.error("Quest with id {} doesn't exists", id);
                return new EntityNotFoundException(String.format("Quest with name %d doesn't exists", id));
            });
    }

    public Collection<QuestDto> findCharactersQuests(Long characterId) {
        Character character = characterService.getCharacter(characterId);
        return questRepository.findByCharacters(character)
            .stream()
            .map(questMapper::mapToDto)
            .toList();
    }

    public QuestDto createQuest(CreateQuestDto createQuestDto, Principal principal) {
        Player player = (Player) playerDetailsService.loadUserByUsername(principal.getName());
        if (player.getGamemasters().isEmpty()) {
            log.error("Player is not a gamemaster in any room");
            throw new NotGamemasterException("Player is not a gamemaster in any room");
        }
        Quest quest = new Quest();
        quest.setName(createQuestDto.name());
        quest.setDescription(createQuestDto.description());
        return questMapper.mapToDto(questRepository.save(quest));
    }

}
