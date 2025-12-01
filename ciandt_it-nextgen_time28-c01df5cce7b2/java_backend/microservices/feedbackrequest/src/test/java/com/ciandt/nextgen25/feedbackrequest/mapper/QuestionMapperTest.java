package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.QuestionDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Question;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionMapperTest {

    private final QuestionMapper mapper = new QuestionMapper();

    @Test
    void shouldMapEntityToDTO() {
        // Arrange
        Question entity = new Question("What are the strengths of this colleague?");

        // Act
        QuestionDTO dto = mapper.toDTO(entity);

        // Assert
        assertEquals("What are the strengths of this colleague?", dto.getText());
    }

    @Test
    void shouldMapDTOToEntity() {
        // Arrange
        QuestionDTO dto = new QuestionDTO("What are the strengths of this colleague?");

        // Act
        Question entity = mapper.toEntity(dto);

        // Assert
        assertEquals("What are the strengths of this colleague?", entity.getText());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        // Act & Assert
        assertNull(mapper.toDTO(null));
    }

    @Test
    void shouldReturnNullWhenDTOIsNull() {
        // Act & Assert
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldMapEntityListToDTOList() {
        // Arrange
        List<Question> entities = Arrays.asList(
                new Question("What are the strengths of this colleague?"),
                new Question("What are the areas for improvement?")
        );

        // Act
        List<QuestionDTO> dtos = mapper.toDTOList(entities);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("What are the strengths of this colleague?", dtos.get(0).getText());
        assertEquals("What are the areas for improvement?", dtos.get(1).getText());
    }

    @Test
    void shouldMapDTOListToEntityList() {
        // Arrange
        List<QuestionDTO> dtos = Arrays.asList(
                new QuestionDTO("What are the strengths of this colleague?"),
                new QuestionDTO("What are the areas for improvement?")
        );

        // Act
        List<Question> entities = mapper.toEntityList(dtos);

        // Assert
        assertEquals(2, entities.size());
        assertEquals("What are the strengths of this colleague?", entities.get(0).getText());
        assertEquals("What are the areas for improvement?", entities.get(1).getText());
    }

    @Test
    void shouldReturnEmptyListWhenEntityListIsNull() {
        // Act
        List<QuestionDTO> result = mapper.toDTOList(null);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldReturnEmptyListWhenDTOListIsNull() {
        // Act
        List<Question> result = mapper.toEntityList(null);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }
}