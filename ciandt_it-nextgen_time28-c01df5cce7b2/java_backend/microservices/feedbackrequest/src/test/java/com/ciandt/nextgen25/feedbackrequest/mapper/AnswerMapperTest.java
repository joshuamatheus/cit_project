package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.AnswerDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Answer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnswerMapperTest {

    private final AnswerMapper mapper = new AnswerMapper();

    @Test
    void shouldMapEntityToDTO() {
        // Arrange
        Answer entity = new Answer("john.doe@example.com", "Excellent performance");

        // Act
        AnswerDTO dto = mapper.toDTO(entity);

        // Assert
        assertEquals("john.doe@example.com", dto.getAppraiserEmail());
        assertEquals("Excellent performance", dto.getText());
    }

    @Test
    void shouldMapDTOToEntity() {
        // Arrange
        AnswerDTO dto = new AnswerDTO("john.doe@example.com", "Excellent performance");

        // Act
        Answer entity = mapper.toEntity(dto);

        // Assert
        assertEquals("john.doe@example.com", entity.getAppraiserEmail());
        assertEquals("Excellent performance", entity.getText());
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
        List<Answer> entities = Arrays.asList(
                new Answer("john.doe@example.com", "Excellent performance"),
                new Answer("jane.doe@example.com", "Good teamwork")
        );

        // Act
        List<AnswerDTO> dtos = mapper.toDTOList(entities);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("john.doe@example.com", dtos.get(0).getAppraiserEmail());
        assertEquals("Excellent performance", dtos.get(0).getText());
        assertEquals("jane.doe@example.com", dtos.get(1).getAppraiserEmail());
        assertEquals("Good teamwork", dtos.get(1).getText());
    }

    @Test
    void shouldMapDTOListToEntityList() {
        // Arrange
        List<AnswerDTO> dtos = Arrays.asList(
                new AnswerDTO("john.doe@example.com", "Excellent performance"),
                new AnswerDTO("jane.doe@example.com", "Good teamwork")
        );

        // Act
        List<Answer> entities = mapper.toEntityList(dtos);

        // Assert
        assertEquals(2, entities.size());
        assertEquals("john.doe@example.com", entities.get(0).getAppraiserEmail());
        assertEquals("Excellent performance", entities.get(0).getText());
        assertEquals("jane.doe@example.com", entities.get(1).getAppraiserEmail());
        assertEquals("Good teamwork", entities.get(1).getText());
    }

    @Test
    void shouldReturnEmptyListWhenEntityListIsNull() {
        // Act
        List<AnswerDTO> result = mapper.toDTOList(null);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldReturnEmptyListWhenDTOListIsNull() {
        // Act
        List<Answer> result = mapper.toEntityList(null);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }
}