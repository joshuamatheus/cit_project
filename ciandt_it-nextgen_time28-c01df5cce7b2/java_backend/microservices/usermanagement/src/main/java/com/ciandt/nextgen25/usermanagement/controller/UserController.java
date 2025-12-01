package com.ciandt.nextgen25.usermanagement.controller;
import java.util.List;
import com.ciandt.nextgen25.usermanagement.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciandt.nextgen25.usermanagement.dto.UserEditDto;
import com.ciandt.nextgen25.usermanagement.dto.UserResponseDto;
import com.ciandt.nextgen25.usermanagement.dto.UserTypeResponseDTO;
import com.ciandt.nextgen25.usermanagement.mapper.UserMapper;
import com.ciandt.nextgen25.usermanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ciandt.nextgen25.usermanagement.dto.UserHasPasswordDto;
import com.ciandt.nextgen25.usermanagement.dto.UserInfoDTO;
import com.ciandt.nextgen25.usermanagement.dto.UserPasswordRegistrationDto;
import com.ciandt.nextgen25.usermanagement.dto.UserRegisterDTO;
import com.ciandt.nextgen25.usermanagement.dto.ListUserResponseDTO;
import com.ciandt.nextgen25.usermanagement.dto.LoginRequestDTO;
import com.ciandt.nextgen25.usermanagement.dto.TokenResponseDTO;
import com.ciandt.nextgen25.usermanagement.dto.UserDTO;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ciandt.nextgen25.security.annotation.HasType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @HasType("ADMIN")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponseDto responseDto = userMapper.toDto(user);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    @HasType("ADMIN")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserEditDto newData) {
        User foundUser = userService.findById(id);
        userMapper.updateUserFromDto(foundUser, newData);
        User updatedUser = userService.update(foundUser);
        UserResponseDto responseDto = userMapper.toDto(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserHasPasswordDto> getByEmail(@RequestParam(required = true) String email) {
        User user = userService.findUserByEmail(email);
        boolean hasPassword = user.getPassword() != null;
        UserHasPasswordDto userDto = new UserHasPasswordDto(user.getName(), hasPassword, user.getEmail());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register-password")
    public ResponseEntity<Void> registerPassword(@RequestBody @Valid UserPasswordRegistrationDto dto) {
        userService.registerPassword(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin")
    @HasType("ADMIN") //apenas admins podem usar esse end-point
    public ResponseEntity<List<ListUserResponseDTO>> getAllUsers() {
        List<ListUserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deactivate/{id}")
    @HasType("ADMIN")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
 
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) throws JsonProcessingException, IllegalArgumentException, JWTCreationException {
        TokenResponseDTO tokenResponse = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(tokenResponse);
    }
 
    @GetMapping("/type")
    public ResponseEntity<UserTypeResponseDTO> getCurrentUserType(@RequestHeader(value = "Authorization", required = false) String authHeader) throws JsonProcessingException, JsonMappingException {    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("No valid token provided");
        }
        String token =  authHeader.replace("Bearer ", "");
        UserTypeResponseDTO userType = userService.getCurrentUserType(token);
        return ResponseEntity.ok(userType);
    }

    @GetMapping("/pdm/{pdmId}/collaborators")
    public ResponseEntity<List<UserInfoDTO>> getCollaboratorsByPdmId(
            @PathVariable Long pdmId) {
        List<UserInfoDTO> collaborators = userService.getCollaboratorsByPdmId(pdmId);
        return ResponseEntity.ok(collaborators);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = userMapper.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}