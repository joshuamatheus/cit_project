package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.AppraiserDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Appraiser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppraiserMapperTest {

    private final AppraiserMapper mapper = new AppraiserMapper();

    @Test
    void shouldMapEntityToDTO() {
        // Arrange
        Appraiser entity = new Appraiser("john.doe@example.com");

        // Act
        AppraiserDTO dto = mapper.toDTO(entity);

        // Assert
        assertEquals("john.doe@example.com", dto.getEmail());
    }

    @Test
    void shouldMapDTOToEntity() {
        // Arrange
        AppraiserDTO dto = new AppraiserDTO("john.doe@example.com");

        // Act
        Appraiser entity = mapper.toEntity(dto);

        // Assert
        assertEquals("john.doe@example.com", entity.getEmail());
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
        List<Appraiser> entities = Arrays.asList(
                new Appraiser("john.doe@example.com"),
                new Appraiser("jane.doe@example.com")
        );

        // Act
        List<AppraiserDTO> dtos = mapper.toDTOList(entities);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("john.doe@example.com", dtos.get(0).getEmail());
        assertEquals("jane.doe@example.com", dtos.get(1).getEmail());
    }

    @Test
    void shouldMapDTOListToEntityList() {
        // Arrange
        List<AppraiserDTO> dtos = Arrays.asList(
                new AppraiserDTO("john.doe@example.com"),
                new AppraiserDTO("jane.doe@example.com")
        );

        // Act
        List<Appraiser> entities = mapper.toEntityList(dtos);

        // Assert
        assertEquals(2, entities.size());
        assertEquals("john.doe@example.com", entities.get(0).getEmail());
        assertEquals("jane.doe@example.com", entities.get(1).getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenEntityListIsNull() {
        // Act
        List<AppraiserDTO> result = mapper.toDTOList(null);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldReturnEmptyListWhenDTOListIsNull() {
        // Act
        List<Appraiser> result = mapper.toEntityList(null);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }
}