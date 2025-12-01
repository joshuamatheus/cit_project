package com.ciandt.nextgen25.usermanagement.service;

import com.ciandt.nextgen25.usermanagement.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.exceptions.EmailAlreadyExistsException;
import com.ciandt.nextgen25.usermanagement.exceptions.SelfPdmException;
import com.ciandt.nextgen25.usermanagement.exceptions.UserAlreadyExistsException;
import com.ciandt.nextgen25.usermanagement.exceptions.EntityNotFoundException;
import com.ciandt.nextgen25.usermanagement.exceptions.UserAlreadyHasAPasswordException;
import com.ciandt.nextgen25.usermanagement.exceptions.UserNotFoundException;
import com.ciandt.nextgen25.usermanagement.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ciandt.nextgen25.security.entity.LoggedUser;
import com.ciandt.nextgen25.security.service.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtTokenService jwtTokenService;
    private final PasswordManager passwordManager;

    @Transactional
    public User registerUser(UserRegisterDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        
        if (repository.findByEmail(userDTO.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }
        
        User newUser = new User();
        newUser.setEmail(userDTO.email());
        newUser.setName(userDTO.name());
        newUser.setType(userDTO.type());
        newUser.setPositionMap(userDTO.positionMap());
        newUser.setRole(userDTO.role());
        newUser.setActive(true);
        
        if (userDTO.pdmEmail() != null && !userDTO.pdmEmail().isEmpty()) {
            User pdm = repository.findByEmail(userDTO.pdmEmail())
                .orElseThrow(() -> new UserNotFoundException("PDM with email " + userDTO.pdmEmail() + " not found"));
            newUser.setPdm(pdm);
        }
        
        log.info("Registrando novo usuário com email: {}", userDTO.email());
        
        return repository.save(newUser);
    }
    
    public User update(User user) {
        boolean emailExists = repository.existsByEmailAndIdNot(user.getEmail(), user.getId());
        if (emailExists) {
            throw new UserAlreadyExistsException("The email is already in use by another user.");
        }

        if (user.getPdm() != null && user.getPdm().getId().equals(user.getId())) {
            throw new SelfPdmException("A user cannot be their own PDM.");
        }

        return repository.save(user);
    }
    
    public User findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException("User with id = " + id + " not found.")
                );
    }
    public User findUserByEmail(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    public void registerPassword(String email, String password) {
        User user = findUserByEmail(email);
        
        if (user.getPassword() != null) {
            throw new UserAlreadyHasAPasswordException("User already has a password");
        }

        user.setPassword(encoder.encode(password));
        repository.save(user);
    }

    public List<ListUserResponseDTO> getAllUsers() {
        List<User> users = repository.findByTypeNotAndActiveTrue(UserType.ADMIN);
        return users.stream()
                .map(user -> ListUserResponseDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .userType(user.getType().name()) 
                        .build())
                .collect(Collectors.toList());
    }

    public void deactivateUser(Long userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setActive(false);
        user.setDeactivationDate(LocalDateTime.now());
        repository.save(user);
    }

    public TokenResponseDTO authenticateUser(String email, String password) throws JsonProcessingException, IllegalArgumentException, JWTCreationException {
        User user = findUserByEmail(email);

        if (user.getPassword() == null) {
            throw new BadCredentialsException("User has not set a password");
        }

        if (!passwordManager.validateUser(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
            LoggedUser loggedUser = new LoggedUser(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getType().toString(),
                user.getPdm() != null ? user.getPdm().getId() : null
            );
            
            String token = jwtTokenService.generateToken(loggedUser);
            long expiresIn = jwtTokenService.getExpirationTimeInSeconds();

            return new TokenResponseDTO(token, expiresIn, "Bearer");
        
    }
        
    public UserTypeResponseDTO getCurrentUserType(String token) throws JsonProcessingException, JsonMappingException {
        // Extrair o usuário do token
        LoggedUser loggedUser = jwtTokenService.getUserFromToken(token);
        
        // Obter o usuário completo do banco de dados para garantir dados atualizados
        User user = repository.findById(loggedUser.getId())
            .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found in the database."));
        
        return new UserTypeResponseDTO(
            user.getType()
        );
    }

    public List<UserInfoDTO> getCollaboratorsByPdmId(Long pdmId) {
        List<User> collaborators = repository.findByPdmId(pdmId);
        return collaborators.stream()
                .map(user -> new UserInfoDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().toString(),
                        user.getPositionMap().toString()))
                .collect(Collectors.toList());
    }
}