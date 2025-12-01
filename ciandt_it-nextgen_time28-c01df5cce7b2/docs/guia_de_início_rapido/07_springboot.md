# Spring Boot e os Building Blocks

Com spring boot se torna bastante simples de implementar os padrões táticos requeridos nesse projeto

## 1. Entity

As *Entities* são representações de objetos do domínio que possuem uma identidade única e podem ter seu estado alterado ao longo do tempo. No Spring Boot, as entidades são frequentemente anotadas com `@Entity`.

### Exemplo:

```java
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private String email;

    // Construtores, getters e setters

    public User() {}

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters e Setters
}
```

## 2. Repository

O *Repository* atua como um intermediário entre a aplicação e a camada de persistência de dados. No Spring Boot, isso é frequentemente feito usando o Spring Data JPA, que simplifica a implementação de repositórios.

### Exemplo:

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Método para encontrar um usuário pelo e-mail
    User findByEmail(String email);
    
    // Método para encontrar todos os usuários com idade maior que um valor específico
    @Query("SELECT u FROM User u WHERE u.age > :age")
    List<User> findAllOlderThan(@Param("age") int age);
}
```

## 3. Service

O *Service* é responsável por implementar a lógica de negócios da aplicação. Ele orquestra as interações entre as entidades e os repositórios.

### Exemplo:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

## 4. Controller

O *Controller* lida com as requisições do usuário e coordena a interação entre a interface do usuário e os serviços. No Spring Boot, os controladores são anotados com `@RestController`.

### Exemplo:
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 5. TDD

Testar utilizando @SpringBootTest com MockMvc é uma prática eficaz que se alinha aos princípios do Desenvolvimento Orientado a Testes (TDD). Essa abordagem permite validar as interações do controlador com o serviço de forma isolada, garantindo que a lógica de manipulação de usuários funcione corretamente, sem depender de componentes externos como um banco de dados real. Ao criar testes antes da implementação, o TDD ajuda a definir claramente os requisitos e comportamentos esperados do sistema, promovendo a escrita de código mais robusto. Essa combinação resulta em um ciclo de desenvolvimento mais eficiente, onde a funcionalidade do sistema pode ser constantemente validada e refinada, assegurando que ele permaneça confiável e adaptável à medida que evolui.

```java
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserNotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isNoContent());
    }
}
```