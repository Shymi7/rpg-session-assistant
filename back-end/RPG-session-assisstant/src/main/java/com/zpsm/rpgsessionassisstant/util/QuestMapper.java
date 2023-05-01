package com.zpsm.rpgsessionassisstant.util;

import com.zpsm.rpgsessionassisstant.dto.QuestDto;
import com.zpsm.rpgsessionassisstant.model.Quest;
import org.springframework.stereotype.Component;

@Component
public class QuestMapper {

    public QuestDto mapToDto(Quest quest) {
        return new QuestDto(quest.getId(), quest.getName(), quest.getDescription());
    }

}
