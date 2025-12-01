package com.ciandt.nextgen25.usermanagement;

import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.repository.UserRepository;
import com.ciandt.nextgen25.usermanagement.service.PasswordManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UsermanagementApplicationTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordManager passwordManager;

	@InjectMocks
	private UsermanagementApplication usermanagementApplication;

	@BeforeEach
	void setUp() {
		Mockito.when(passwordManager.registerUser(any(String.class), any(String.class))).thenReturn("encryptedPassword");
	}

	@Test
	void testRegisterAdminWithValidEmail() {
		String[] args = {"cadastrar:admin,John Doe,john.doe@ciandt.com,password123"};
		usermanagementApplication.run(args);
		Mockito.verify(userRepository).save(any(User.class));
	}

	@Test
	void testRegisterAdminWithInvalidEmail() {
		String[] args = {"cadastrar:admin,John Doe,john.doe@gmail.com,password123"};
		usermanagementApplication.run(args);
		Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
	}

	@Test
	void testRegisterAdminWithEmptyEmail() {
		String[] args = {"cadastrar:admin,John Doe,,password123"};
		usermanagementApplication.run(args);
		Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
	}

	@Test
	void testRegisterAdminWithDuplicateUser() {
		String[] args = {"cadastrar:admin,John Doe,john.doe@ciandt.com,password123"};
		User existingUser = new User();
		existingUser.setEmail("john.doe@ciandt.com");
		Mockito.when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));
		usermanagementApplication.run(args);
		Mockito.verify(userRepository).save(any(User.class));
	}
}