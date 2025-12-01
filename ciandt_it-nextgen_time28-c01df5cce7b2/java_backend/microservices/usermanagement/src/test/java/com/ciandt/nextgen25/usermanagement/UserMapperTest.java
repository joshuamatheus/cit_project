package com.ciandt.nextgen25.usermanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ciandt.nextgen25.usermanagement.dto.UserEditDto;
import com.ciandt.nextgen25.usermanagement.dto.UserResponseDto;
import com.ciandt.nextgen25.usermanagement.entity.PositionMap;
import com.ciandt.nextgen25.usermanagement.entity.Role;
import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import com.ciandt.nextgen25.usermanagement.exceptions.EntityNotFoundException;
import com.ciandt.nextgen25.usermanagement.exceptions.InvalidPdmEmailException;
import com.ciandt.nextgen25.usermanagement.mapper.UserMapper;
import com.ciandt.nextgen25.usermanagement.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserMapper userMapper;

    private UserEditDto userEditDto;
    private User user;

    @BeforeEach
    public void setUp() {
        userEditDto = new UserEditDto(
            "John Doe",
            "john.doe@example.com",
            UserType.COLLABORATOR,
            PositionMap.SENIOR,
            Role.DEVELOPER,
            "pdm@example.com"
        );

        user = new User();
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setType(UserType.PDM);
        user.setPositionMap(PositionMap.JUNIOR);
        user.setRole(Role.DEVELOPER);
    }

    @Test
    public void testToEntity() {
        User mappedUser = userMapper.toEntity(userEditDto);

        assertNotNull(mappedUser);
        assertEquals(userEditDto.name(), mappedUser.getName());
        assertEquals(userEditDto.email(), mappedUser.getEmail());
        assertEquals(userEditDto.type(), mappedUser.getType());
        assertEquals(userEditDto.positionMap(), mappedUser.getPositionMap());
        assertEquals(userEditDto.role(), mappedUser.getRole());
        assertNull(mappedUser.getId());  // ID should not be set by the mapper
    }

    @Test
    public void testToDto() {
        UserResponseDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.id());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getEmail(), dto.email());
        assertEquals(user.getType(), dto.type());
        assertEquals(user.getPositionMap(), dto.positionMap());
        assertEquals(user.getRole(), dto.role());
    }

    @Test
    public void testUpdateUserFromDto() {
        User pdm = new User();
        pdm.setEmail("pdm@example.com");
        when(userService.findUserByEmail("pdm@example.com")).thenReturn(pdm);

        userMapper.updateUserFromDto(user, userEditDto);

        assertEquals(userEditDto.name(), user.getName());
        assertEquals(userEditDto.email(), user.getEmail());
        assertEquals(userEditDto.type(), user.getType());
        assertEquals(userEditDto.positionMap(), user.getPositionMap());
        assertEquals(userEditDto.role(), user.getRole());
        assertEquals(pdm, user.getPdm());
    }

    @Test
    public void testUpdateUserFromDtoWithNullPdm() {
        userEditDto = new UserEditDto(
            "John Doe",
            "john.doe@example.com",
            UserType.COLLABORATOR,
            PositionMap.SENIOR,
            Role.DEVELOPER,
            null
        );

        userMapper.updateUserFromDto(user, userEditDto);

        assertEquals(userEditDto.name(), user.getName());
        assertEquals(userEditDto.email(), user.getEmail());
        assertEquals(userEditDto.type(), user.getType());
        assertEquals(userEditDto.positionMap(), user.getPositionMap());
        assertEquals(userEditDto.role(), user.getRole());
        assertNull(user.getPdm());
    }

    @Test
    public void testUpdateUserFromDtoWithInvalidPdmEmail() {
        when(userService.findUserByEmail("invalid@example.com"))
            .thenThrow(new EntityNotFoundException("User not found"));

        UserEditDto invalidPdmDto = new UserEditDto(
            "John Doe",
            "john.doe@example.com",
            UserType.COLLABORATOR,
            PositionMap.SENIOR,
            Role.DEVELOPER,
            "invalid@example.com"
        );

        assertThrows(InvalidPdmEmailException.class, () -> {
            userMapper.updateUserFromDto(user, invalidPdmDto);
        });
    }

    @Test
    public void testToEntityWithNullValues() {
        UserEditDto nullDto = new UserEditDto(null, null, null, null, null, null);
        User mappedUser = userMapper.toEntity(nullDto);

        assertNotNull(mappedUser);
        assertNull(mappedUser.getName());
        assertNull(mappedUser.getEmail());
        assertNull(mappedUser.getType());
        assertNull(mappedUser.getPositionMap());
        assertNull(mappedUser.getRole());
    }

    @Test
    public void testToDtoWithNullValues() {
        User nullUser = new User();
        UserResponseDto dto = userMapper.toDto(nullUser);

        assertNotNull(dto);
        assertNull(dto.id());
        assertNull(dto.name());
        assertNull(dto.email());
        assertNull(dto.type());
        assertNull(dto.positionMap());
        assertNull(dto.role());
    }
}