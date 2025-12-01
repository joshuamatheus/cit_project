package com.ciandt.nextgen25.usermanagement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

import com.ciandt.nextgen25.usermanagement.dto.UserInfoDTO;
import com.ciandt.nextgen25.usermanagement.entity.PositionMap;
import com.ciandt.nextgen25.usermanagement.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.ciandt.nextgen25.security.entity.LoggedUser;
import com.ciandt.nextgen25.security.service.JwtTokenService;
import com.ciandt.nextgen25.usermanagement.dto.ListUserResponseDTO;
import com.ciandt.nextgen25.usermanagement.dto.TokenResponseDTO;
import com.ciandt.nextgen25.usermanagement.dto.UserTypeResponseDTO;
import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import com.ciandt.nextgen25.usermanagement.exceptions.SelfPdmException;
import com.ciandt.nextgen25.usermanagement.exceptions.UserAlreadyExistsException;
import com.ciandt.nextgen25.usermanagement.repository.UserRepository;
import com.ciandt.nextgen25.usermanagement.service.PasswordManager;
import com.ciandt.nextgen25.usermanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ciandt.nextgen25.usermanagement.exceptions.EntityNotFoundException;
import com.ciandt.nextgen25.usermanagement.exceptions.UserAlreadyHasAPasswordException;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

     @Mock
    private PasswordManager passwordManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("123456");
        user.setType(UserType.COLLABORATOR);
        user.setPdm(null);
    }

    @Test
    void testFindUserByEmail_ExistingUser() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        User result = userService.findUserByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void testFindUserByEmail_NonExistingUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.findUserByEmail("nonexistent@example.com");
        });
    }

    @Test
    void testRegisterPassword_Success() {
        user.setPassword(null);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        userService.registerPassword("john.doe@example.com", "newPassword");

        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testRegisterPassword_UserAlreadyHasPassword() {
        user.setPassword("existingPassword");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyHasAPasswordException.class, () -> {
            userService.registerPassword("john.doe@example.com", "newPassword");
        });
    }

    @Test
    void testRegisterPassword_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.registerPassword("nonexistent@example.com", "newPassword");
        });
    }

    @Test
    void getAllUsersListOfUserResponseDTO() {
        // Arrange
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john@ciandt.com");
        user1.setType(UserType.COLLABORATOR);

        User user2 = new User();
        user2.setName("Jane Doe");
        user2.setEmail("jane@ciandt.com");
        user2.setType(UserType.PDM);

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findByTypeNotAndActiveTrue(UserType.ADMIN)).thenReturn(users);

        // Act
        List<ListUserResponseDTO> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john@ciandt.com", result.get(0).getEmail());
        assertEquals("COLLABORATOR", result.get(0).getUserType());
        assertEquals("Jane Doe", result.get(1).getName());
        assertEquals("jane@ciandt.com", result.get(1).getEmail());
        assertEquals("PDM", result.get(1).getUserType());
    }

    @Test
    void testDeactivateUser_Success(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(true);
        user.setDeactivationDate(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deactivateUser(userId);

        // Assert
        assertFalse(user.isActive());
        assertNotNull(user.getDeactivationDate());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void testDeactivateUser_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.deactivateUser(userId));
        verify(userRepository).findById(userId);
        verify(userRepository,  never()).save(any(User.class));
    }

    @Test
    public void testFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(1L);
        });

        assertEquals("User with id = 1 not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateUserSuccessfully() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");

        when(userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(updatedUser);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUserEmailAlreadyExists() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("existing@example.com");

        when(userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.update(updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUserSelfPdm() {
        User pdm = new User();
        pdm.setId(1L);
        user.setPdm(pdm);

        Exception exception = assertThrows(SelfPdmException.class, () -> {
            userService.update(user);
        });

        assertEquals("A user cannot be their own PDM.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUserWithDifferentPdm() {
        User pdm = new User();
        pdm.setId(2L);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");
        updatedUser.setPdm(pdm);

        when(userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(updatedUser);

        assertNotNull(result);
        assertEquals(2L, result.getPdm().getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUserWithSelfAsPdm() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setPdm(updatedUser);

        when(userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())).thenReturn(false);

        assertThrows(SelfPdmException.class, () -> userService.update(updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_SuccessfulAuthentication() throws JsonProcessingException, IllegalArgumentException, JWTCreationException {
    // Arrange
    String email = "test@example.com";
    String password = "password";
    User user = new User();
    user.setId(1L);
    user.setName("Test User");
    user.setEmail(email);
    user.setPassword("encodedPassword");
    user.setType(UserType.COLLABORATOR);

    when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
    when(passwordManager.validateUser(password, user.getPassword())).thenReturn(true);
    when(jwtTokenService.generateToken(any(LoggedUser.class))).thenReturn("token");
    when(jwtTokenService.getExpirationTimeInSeconds()).thenReturn(3600L);

    // Act
    TokenResponseDTO response = userService.authenticateUser(email, password);

    // Assert
    assertNotNull(response);
    assertEquals("token", response.getToken());
    assertEquals(3600L, response.getExpiresIn());
    assertEquals("Bearer", response.getTokenType());

    verify(userRepository).findByEmail(email);
    verify(passwordManager).validateUser(password, user.getPassword());
    verify(jwtTokenService).generateToken(any(LoggedUser.class));
    verify(jwtTokenService).getExpirationTimeInSeconds();
}

    @Test
    void authenticateUser_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.authenticateUser(email, password));

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(passwordManager, jwtTokenService);
    }

    @Test
    void authenticateUser_NoPasswordSet() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(null);

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> userService.authenticateUser(email, password));

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(passwordManager, jwtTokenService);
    }

    @Test
    void authenticateUser_InvalidPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(passwordManager.validateUser(password, user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> userService.authenticateUser(email, password));

        verify(userRepository).findByEmail(email);
        verify(passwordManager).validateUser(password, user.getPassword());
        verifyNoInteractions(jwtTokenService);
    }

    @Test
    void getCurrentUserType_Success() throws JsonProcessingException {
        // Arrange
        String token = "valid-token";
        LoggedUser loggedUser = new LoggedUser(1L, "Test User", "test@example.com", "COLLABORATOR", null);
        User user = new User();
        user.setId(1L);
        user.setType(UserType.COLLABORATOR);
        when(jwtTokenService.getUserFromToken(token)).thenReturn(loggedUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Act
        UserTypeResponseDTO response = userService.getCurrentUserType(token);
        // Assert
        assertNotNull(response);
        assertEquals(UserType.COLLABORATOR, response.getType());
        verify(jwtTokenService).getUserFromToken(token);
        verify(userRepository).findById(1L);
    }
    @Test
    void getCurrentUserType_UserNotFound() throws JsonProcessingException {
        // Arrange
        String token = "valid-token";
        LoggedUser loggedUser = new LoggedUser(1L, "Test User", "test@example.com", "COLLABORATOR", null);
        when(jwtTokenService.getUserFromToken(token)).thenReturn(loggedUser);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> userService.getCurrentUserType(token));
        
        assertEquals("Authenticated user not found in the database.", exception.getMessage());
        verify(jwtTokenService).getUserFromToken(token);
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetCollaboratorsByPdmId_Success() {
        // Arrange
        Long pdmId = 1L;
        User user1 = new User();
        user1.setId(2L);
        user1.setEmail("collaborator1@example.com");
        user1.setRole(Role.DEVELOPER);
        user1.setPositionMap(PositionMap.JUNIOR);
        User user2 = new User();
        user2.setId(3L);
        user2.setEmail("collaborator2@example.com");
        user2.setRole(Role.UX_DESIGNER);
        user2.setPositionMap(PositionMap.INTERN);
        List<User> collaborators = Arrays.asList(user1, user2);
        when(userRepository.findByPdmId(pdmId)).thenReturn(collaborators);
        // Act
        List<UserInfoDTO> result = userService.getCollaboratorsByPdmId(pdmId);
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verificar o primeiro colaborador
        assertEquals(2L, result.get(0).getId());
        assertEquals("collaborator1@example.com", result.get(0).getEmail());
        assertEquals("DEVELOPER", result.get(0).getRole());
        assertEquals("JUNIOR", result.get(0).getPositionMap());
        // Verificar o segundo colaborador
        assertEquals(3L, result.get(1).getId());
        assertEquals("collaborator2@example.com", result.get(1).getEmail());
        assertEquals("UX_DESIGNER", result.get(1).getRole());
        assertEquals("INTERN", result.get(1).getPositionMap());
        // Verificar que o método do repositório foi chamado corretamente
        verify(userRepository).findByPdmId(pdmId);
    }
    @Test
    void testGetCollaboratorsByPdmId_EmptyList() {
        // Arrange
        Long pdmId = 1L;
        when(userRepository.findByPdmId(pdmId)).thenReturn(Collections.emptyList());
        // Act
        List<UserInfoDTO> result = userService.getCollaboratorsByPdmId(pdmId);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Verificar que o método do repositório foi chamado corretamente
        verify(userRepository).findByPdmId(pdmId);
    }
}