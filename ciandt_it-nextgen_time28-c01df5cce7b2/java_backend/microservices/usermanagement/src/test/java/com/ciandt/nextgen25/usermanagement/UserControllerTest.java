package com.ciandt.nextgen25.usermanagement;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciandt.nextgen25.usermanagement.dto.*;
import com.ciandt.nextgen25.usermanagement.repository.UserRepository;
import com.ciandt.nextgen25.usermanagement.validation.FirstUserValidator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ciandt.nextgen25.usermanagement.controller.UserController;
import com.ciandt.nextgen25.usermanagement.entity.PositionMap;
import com.ciandt.nextgen25.usermanagement.entity.Role;
import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import com.ciandt.nextgen25.usermanagement.exceptions.EmailAlreadyExistsException;
import com.ciandt.nextgen25.usermanagement.exceptions.EntityNotFoundException;
import com.ciandt.nextgen25.usermanagement.exceptions.GlobalExceptionHandler;
import com.ciandt.nextgen25.usermanagement.exceptions.UserNotFoundException;
import com.ciandt.nextgen25.usermanagement.mapper.UserMapper;
import com.ciandt.nextgen25.usermanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Autowired
    private MockMvc mockMvcWithoutValidator;

    private User user;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Mock
    private FirstUserValidator firstUserValidator;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        MockitoAnnotations.openMocks(this);
        doNothing().when(firstUserValidator).validate(any(), any());
        UserMapper userMapper = new UserMapper(userService);
        UserController userController = new UserController(userService, userMapper);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(firstUserValidator)
                .build();

        this.mockMvcWithoutValidator = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        user = new User(
                1L,
                "John Doe",
                "john.doe@example.com",
                "123",
                UserType.PDM,
                Role.DEVELOPER,
                PositionMap.INTERN,
                null,
                true,
                null);
    }

    @Test
    public void testGetByEmail_ExistingUser() throws Exception {
        when(userService.findUserByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/by-email")
                .param("email", "john.doe@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.hasPassword").value(true))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testGetByEmail_NonExistingUser() throws Exception {
        when(userService.findUserByEmail("nonexistent@example.com"))
                .thenThrow(new EntityNotFoundException("User not found."));

        mockMvc.perform(get("/by-email")
                .param("email", "nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found."));
    }

    @Test
    public void testRegisterPassword_ValidInput() throws Exception {
        UserPasswordRegistrationDto dto = new UserPasswordRegistrationDto();
        dto.setEmail("john.doe@example.com");
        dto.setPassword("ValidP@ssw0rd123!");
        dto.setConfirmPassword("ValidP@ssw0rd123!");

        doNothing().when(userService).registerPassword(anyString(), anyString());

        mockMvc.perform(post("/register-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userService).registerPassword("john.doe@example.com", "ValidP@ssw0rd123!");
    }

    @Test
    public void testRegisterPassword_InvalidInput() throws Exception {
        UserPasswordRegistrationDto dto = new UserPasswordRegistrationDto();
        dto.setEmail("invalid-email");
        dto.setPassword("weak");
        dto.setConfirmPassword("weak");

        // Se necessário, configure o comportamento específico do validator para este teste
        doThrow(new ConstraintViolationException(Collections.emptySet())).when(firstUserValidator).validate(any(), any());

        mockMvcWithoutValidator.perform(post("/register-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testRegisterFirstUser() throws Exception {

        UserEditDto userEditDto = new UserEditDto(
                "John Doe",
                "john.doe@example.com",
                UserType.PDM,
                PositionMap.MANAGER_MASTER,
                Role.DEVELOPER,
                null); // pdmEmail deve ser null para o primeiro usuário

        String userDtoJson = objectMapper.writeValueAsString(userEditDto);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isCreated());

        verify(userService).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testRegisterUser() throws Exception {

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(
                "John Doe",
                "john.doe@ciandt.com",
                UserType.ADMIN,
                PositionMap.MANAGER_MASTER,
                Role.DEVELOPER,
                "pdm@ciandt.com");

        String userDtoJson = objectMapper.writeValueAsString(userRegisterDTO);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isCreated());

        verify(userService).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testRegisterUserWithExistingEmail() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(
                "John Doe",
                "john.doe@ciandt.com",
                UserType.ADMIN,
                PositionMap.MANAGER_MASTER,
                Role.DEVELOPER,
                "pdm@ciandt.com");

        doThrow(new EmailAlreadyExistsException("Email já está em uso"))
                .when(userService).registerUser(any(UserRegisterDTO.class));

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        mockMvc.perform(get("/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.type").value("PDM"))
            .andExpect(jsonPath("$.role").value("DEVELOPER"))
            .andExpect(jsonPath("$.positionMap").value("INTERN"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.findById(1L)).thenThrow(new EntityNotFoundException("User with id = \" + 1L + \" not found."));
        mockMvc.perform(get("/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("User with id = \" + 1L + \" not found."));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserEditDto userEditDto = new UserEditDto(
            "John Doe Updated",
            "john.doe.updated@ciandt.com",
            UserType.PDM,
            PositionMap.JUNIOR,
            Role.DEVELOPER,
            "pdm@exemple.com"
        );

        when(userService.findById(1L)).thenReturn(user);
        user.setName(userEditDto.name());
        user.setEmail(userEditDto.email());
        user.setType(userEditDto.type());
        user.setPositionMap(userEditDto.positionMap());
        user.setRole(userEditDto.role());
        when(userService.update(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEditDto)))
            .andExpect(status().isOk());
    }

    @Test
    @Disabled("Teste incompleto: implementação pendente")
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllUsersListOfUserResponseDTO() {
        UserMapper userMapper = new UserMapper(userService);
        UserController userController = new UserController(userService, userMapper);

        // Arrange
        ListUserResponseDTO user1 = ListUserResponseDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .userType("COLLABORATOR")
                .build();

        ListUserResponseDTO user2 = ListUserResponseDTO.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .userType("PDM")
                .build();

        List<ListUserResponseDTO> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<ListUserResponseDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
        assertEquals("john@example.com", response.getBody().get(0).getEmail());
        assertEquals("COLLABORATOR", response.getBody().get(0).getUserType());
        assertEquals("Jane Doe", response.getBody().get(1).getName());
        assertEquals("jane@example.com", response.getBody().get(1).getEmail());
        assertEquals("PDM", response.getBody().get(1).getUserType());
    }

    @Test
    public void testDeactivateUser_Success() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deactivateUser(userId);

        mockMvc.perform(delete("/deactivate/" + userId))
                .andExpect(status().isNoContent());

        verify(userService).deactivateUser(userId);
    }

        @Test
        public void testDeactivateUser_UserNotFound() throws Exception {
                Long userId = 1L;

                // Simula a exceção ao tentar desativar um usuário que não existe
                doThrow(new UserNotFoundException("User not found.")).when(userService).deactivateUser(userId);

                // Realiza a chamada ao endpoint e verifica a resposta
                mockMvc.perform(delete("/deactivate/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("User not found."));

                // Verifica se o método do service foi chamado
                verify(userService).deactivateUser(userId);
        }

         @Test
        public void testGetCurrentUserType_Success() throws Exception {
        // Arrange
        String token = "valid-token";
        String authHeader = "Bearer " + token;
        UserTypeResponseDTO responseDTO = new UserTypeResponseDTO(UserType.PDM);
        
        when(userService.getCurrentUserType(token)).thenReturn(responseDTO);
        
        // Act & Assert
        mockMvc.perform(get("/type")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PDM"));
        
        verify(userService).getCurrentUserType(token);
        }
        @Test
        public void testGetCurrentUserType_NoAuthHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No valid token provided"));
        
        verifyNoInteractions(userService);
        }
        @Test
        public void testGetCurrentUserType_InvalidAuthHeader() throws Exception {
        // Arrange
        String authHeader = "InvalidHeader";
        
        // Act & Assert
        mockMvc.perform(get("/type")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No valid token provided"));
        
        verifyNoInteractions(userService);
        }
        @Test
        public void testGetCurrentUserType_UserNotFound() throws Exception {
        // Arrange
        String token = "valid-token";
        String authHeader = "Bearer " + token;
        
        when(userService.getCurrentUserType(token))
                .thenThrow(new EntityNotFoundException("Usuário autenticado não encontrado no banco de dados"));
        
        // Act & Assert
        mockMvc.perform(get("/type")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário autenticado não encontrado no banco de dados"));
        
        verify(userService).getCurrentUserType(token);
        }
        @Test
        public void testGetCurrentUserType_JsonProcessingError() throws Exception {
        // Arrange
        String token = "valid-token";
        String authHeader = "Bearer " + token;
        
        when(userService.getCurrentUserType(token))
                .thenThrow(new JsonProcessingException("Error processing token") {});
        
        // Act & Assert
        mockMvc.perform(get("/type")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        
        verify(userService).getCurrentUserType(token);
        }

    @Test
    public void testGetCollaboratorsByPdmId_Success() throws Exception {
        // Arrange
        Long pdmId = 1L;
        // Crie uma lista de UserInfoDTO para simular a resposta do serviço
        List<UserInfoDTO> collaborators = Arrays.asList(
                new UserInfoDTO(2L,  "collaborator1@example.com", "DEVELOPER", "JUNIOR"),
                new UserInfoDTO(3L,  "collaborator2@example.com", "QA", "JUNIOR")
        );
        // Configure o comportamento esperado do serviço mockado
        when(userService.getCollaboratorsByPdmId(pdmId)).thenReturn(collaborators);
        // Act & Assert
        mockMvc.perform(get("/pdm/{pdmId}/collaborators", pdmId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].email").value("collaborator1@example.com"))
                .andExpect(jsonPath("$[0].role").value("DEVELOPER"))
                .andExpect(jsonPath("$[0].positionMap").value("JUNIOR"))
                .andExpect(jsonPath("$[1].id").value(3L))
                .andExpect(jsonPath("$[1].email").value("collaborator2@example.com"))
                .andExpect(jsonPath("$[1].role").value("QA"))
                .andExpect(jsonPath("$[1].positionMap").value("JUNIOR"));
        // Verifique se o método do serviço foi chamado com o parâmetro correto
        verify(userService).getCollaboratorsByPdmId(pdmId);
    }
    @Test
    public void testGetCollaboratorsByPdmId_EmptyList() throws Exception {
        // Arrange
        Long pdmId = 1L;
        // Configure o serviço para retornar uma lista vazia
        when(userService.getCollaboratorsByPdmId(pdmId)).thenReturn(Collections.emptyList());
        // Act & Assert
        mockMvc.perform(get("/pdm/{pdmId}/collaborators", pdmId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
        verify(userService).getCollaboratorsByPdmId(pdmId);
    }
    @Test
    public void testGetCollaboratorsByPdmId_PdmNotFound() throws Exception {
        // Arrange
        Long nonExistentPdmId = 999L;
        // Configure o serviço para lançar uma exceção quando o PDM não existe
        when(userService.getCollaboratorsByPdmId(nonExistentPdmId))
                .thenThrow(new EntityNotFoundException("PDM with id " + nonExistentPdmId + " not found"));
        // Act & Assert
        mockMvc.perform(get("/pdm/{pdmId}/collaborators", nonExistentPdmId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("PDM with id " + nonExistentPdmId + " not found"));
        verify(userService).getCollaboratorsByPdmId(nonExistentPdmId);
    }
}
