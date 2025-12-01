package com.ciandt.nextgen25.usermanagement;

import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import com.ciandt.nextgen25.usermanagement.repository.UserRepository;

import com.ciandt.nextgen25.usermanagement.service.EmailValidator;
import com.ciandt.nextgen25.usermanagement.service.PasswordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootApplication
public class UsermanagementApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordManager passwordManager;

	@Autowired
	public UsermanagementApplication(UserRepository userRepository, PasswordManager passwordManager) {
		this.userRepository = userRepository;
        this.passwordManager = passwordManager;
    }

	public static void main(String[] args) {
		SpringApplication.run(UsermanagementApplication.class, args);
	}

	@Override
	public void run(String... args) {

		if (args.length > 0) {
			String[] adminCredentials = args[0].split(",");
			switch (adminCredentials[0]) {
				case "cadastrar:admin":

					String name = adminCredentials[1];
					String email = adminCredentials[2];
					String password = adminCredentials[3];
					String encryptedPassword = passwordManager.registerUser(name, password);

					if (!EmailValidator.isValidEmail(email)) {
						System.out.println("E-mail inválido.");
						break;
					} else if (!EmailValidator.hasCiandtDomain(email)) {
						System.out.println("E-mail inválido.");
						break;
					}

					// Salvar o usuário no banco de dados
					try {
						// Criação do usuário
						User adminUser = new User();
						adminUser.setName(name);
						adminUser.setEmail(email);
						adminUser.setPassword(encryptedPassword);
						adminUser.setType(UserType.ADMIN);
						adminUser.setActive(true); // Defina o estado como ativo

						userRepository.save(adminUser);
						System.out.println("Admin cadastrado: " + name);
					} catch (DataIntegrityViolationException e) {
						e.getMessage();
					}
					break;
			}
		}
	}
}