package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.AnswerDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Answer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsável por mapear entre a classe incorporável Answer e sua correspondente DTO.
 */
@Component
public class AnswerMapper {

    public AnswerDTO toDTO(Answer entity) {
        if (entity == null) {
            return null;
        }

        return new AnswerDTO(
                entity.getAppraiserEmail(),
                entity.getText()
        );
    }

    public Answer toEntity(AnswerDTO dto) {
        if (dto == null) {
            return null;
        }

        // Como a classe Answer tem um construtor com todos os argumentos (@AllArgsConstructor),
        // podemos usá-lo diretamente em vez de criar setters individuais
        return new Answer(
                dto.getAppraiserEmail(),
                dto.getText()
        );
    }

    public List<AnswerDTO> toDTOList(List<Answer> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Answer> toEntityList(List<AnswerDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}