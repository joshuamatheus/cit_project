package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.AppraiserDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Appraiser;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppraiserMapper {

    public AppraiserDTO toDTO(Appraiser entity) {
        if (entity == null) {
            return null;
        }

        return new AppraiserDTO( entity.getEmail());
    }

    public Appraiser toEntity(AppraiserDTO dto) {
        if (dto == null) {
            return null;
        }


        return new Appraiser(dto.getEmail());
    }

    public List<AppraiserDTO> toDTOList(List<Appraiser> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Appraiser> toEntityList(List<AppraiserDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}