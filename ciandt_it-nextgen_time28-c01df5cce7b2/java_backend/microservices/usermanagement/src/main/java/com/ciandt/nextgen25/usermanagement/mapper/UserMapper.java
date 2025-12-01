package com.ciandt.nextgen25.usermanagement.mapper;

import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.exceptions.EntityNotFoundException;
import com.ciandt.nextgen25.usermanagement.exceptions.InvalidPdmEmailException;
import com.ciandt.nextgen25.usermanagement.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.ciandt.nextgen25.usermanagement.dto.UserDTO;
import com.ciandt.nextgen25.usermanagement.dto.UserEditDto;
import com.ciandt.nextgen25.usermanagement.dto.UserResponseDto;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserService userService;
    
    public User toEntity(UserEditDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setType(dto.type());
        user.setPositionMap(dto.positionMap());
        user.setRole(dto.role());
        return user;
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getType(),
            user.getPositionMap(),
            user.getRole(),
            user.getPdm() != null ? user.getPdm().getEmail() : null
        );
    }

    public void updateUserFromDto(User user, UserEditDto dto) {
        user.setEmail(dto.email());
        user.setName(dto.name());
        user.setType(dto.type());
        user.setPositionMap(dto.positionMap());
        user.setRole(dto.role());
        
        updateUserPdm(user, dto.pdmEmail());
    }

    private void updateUserPdm(User user, String pdmEmail) {
        if (pdmEmail != null && !pdmEmail.isEmpty()) {
            try {
                User pdm = userService.findUserByEmail(pdmEmail);
                user.setPdm(pdm);
            } catch (EntityNotFoundException e) {
                throw new InvalidPdmEmailException("PDM not found with the provided email: " + pdmEmail);
            }
        } else {
            user.setPdm(null);
        }
    }

    public UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        if (user.getPdm() != null) {
            User pdm = user.getPdm();
            dto.setPdmId(pdm.getId());
            dto.setPdmName(pdm.getName());
            dto.setPdmEmail(pdm.getEmail());
        }
        return dto;
    }
}
