package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.QuestionDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Question;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper {

    public QuestionDTO toDTO(Question entity) {
        if (entity == null) {
            return null;
        }

        return new QuestionDTO(entity.getText());
    }

    public Question toEntity(QuestionDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Question(dto.getText());
    }

    public List<QuestionDTO> toDTOList(List<Question> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Question> toEntityList(List<QuestionDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}