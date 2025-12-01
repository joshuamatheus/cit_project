# Detalhamentos das bibliotecas usadas no projeto

### 1. Spring Boot Starter Data JPA

#### Teoria
O Spring Boot Starter Data JPA é uma biblioteca que facilita a implementação de persistência de dados em aplicações Java através da Java Persistence API (JPA). Ele oferece abstrações e implementações para acessar dados de forma mais simples, utilizando repositórios.

#### Utilidade

- **Abstração da Persistência:** Permite que os desenvolvedores interajam com bancos de dados sem escrever SQL diretamente.

- **Facilidade de Uso:** Com repositórios, você pode realizar operações CRUD (Create, Read, Update, Delete) com poucas linhas de código.

- **Integração com Hibernate:** O Hibernate é o provedor JPA padrão, que oferece ferramentas e funcionalidades avançadas.

#### Exemplo de Uso

**1.1. Configuração no `application.properties`:**

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

**1.2. Entidade:**

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    // Getters e Setters
}
```

**1.3. Repositório:**

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
```

**1.4. Serviço:**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
```

**1.5. Controlador:**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
```

---

### 2. Spring Boot Starter Security

#### Teoria
O Spring Boot Starter Security fornece suporte para autenticação e autorização em aplicações. Utiliza o Spring Security, que é um framework robusto e amplamente utilizado para proteger aplicações Java.

#### Utilidade

- **Autenticação:** Verifica a identidade do usuário.

- **Autorização:** Controla o acesso a recursos com base nas permissões do usuário.

- **Segurança Personalizável:** Permite a configuração de segurança de acordo com as necessidades da aplicação.

#### Exemplo de Uso

**2.1. Configuração de Segurança:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/public/**").permitAll() // Permitir acesso público
                .anyRequest().authenticated() // Requer autenticação para qualquer outra requisição
                .and()
            .formLogin(); // Habilitar login baseado em formulário
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

### 3. Spring Boot Starter Web

#### Teoria
O Spring Boot Starter Web é uma biblioteca que facilita a construção de aplicações web, incluindo serviços RESTful, utilizando o Spring MVC. Ele traz todas as dependências necessárias para desenvolvimento web.

#### Utilidade

- **Facilidade na Criação de APIs:** Permite a criação de endpoints REST de maneira rápida.

- **Suporte a Templating:** Integra-se com bibliotecas de templating como Thymeleaf.

- **Gerenciamento de Requisições e Respostas:** Facilita a manipulação de HTTP.

#### Exemplo de Uso

**3.1. Controlador:**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
```

---

### 4. Spring Boot DevTools

#### Teoria
O Spring Boot DevTools é um conjunto de ferramentas que aprimora a experiência de desenvolvimento. Ele permite que a aplicação reinicie automaticamente quando mudanças são detectadas.

#### Utilidade
- **Recarregamento Automático:** Facilita o desenvolvimento, pois você não precisa reiniciar a aplicação manualmente.
- **Melhorias na Experiência de Desenvolvimento:** Habilita configurações que simplificam o fluxo de trabalho de desenvolvimento.

#### Exemplo de Uso

**4.1. Configuração:**

Basta adicionar a dependência no `pom.xml` e, em seguida, executar a aplicação. Ao fazer alterações no código, a aplicação será reiniciada automaticamente.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

---

### 5. H2 Database

#### Teoria
O H2 é um banco de dados relacional leve que pode ser executado em memória e é usado principalmente para desenvolvimento e testes. Ele é fácil de configurar e não requer instalação.

#### Utilidade

- **Teste Rápido:** Ideal para testes unitários e desenvolvimento rápido.

- **Sem Configuração Complexa:** Basta configurar o JDBC para usar.

- **Console Web:** Oferece um console web para interagir com o banco de dados.

#### Exemplo de Uso

**5.1. Configuração no `application.properties`:**

```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
```

**5.2. Acesso ao Console H2:**

Acesse o console em `http://localhost:8080/h2-console` após iniciar a aplicação.

---

### 6. PostgreSQL

#### Teoria
PostgreSQL é um sistema de gerenciamento de banco de dados relacional objeto (ORDBMS) que é amplamente utilizado por suas robustas funcionalidades e conformidade com padrões.

#### Utilidade

- **Suporte a Transações:** Gerencia transações complexas com segurança.

- **Escalabilidade:** Capaz de lidar com grandes volumes de dados.

- **Extensibilidade:** Permite a criação de tipos de dados personalizados.

#### Exemplo de Uso

**6.1. Configuração no `application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.jpa.hibernate.ddl-auto=update
```

**6.2. Conexão ao Banco:**

Certifique-se de que o servidor PostgreSQL esteja em execução e que o banco de dados e as credenciais estejam corretos.

---

### 7. Lombok

#### Teoria
Lombok é uma biblioteca que ajuda a reduzir a quantidade de código boilerplate, como getters, setters, construtores e métodos `toString`, através de anotações.

#### Utilidade

- **Reduzir Código Boilerplate:** Facilita a escrita de classes de modelo.

- **Facilita a Manutenção:** Menos código resulta em menos erros e facilita a manutenção.

#### Exemplo de Uso

**7.1. Model com Lombok:**

```java
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data // Gera automaticamente métodos getters e setters
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
}
```

---

### 8. Spring Boot Starter Test

#### Teoria
O Spring Boot Starter Test fornece todas as dependências necessárias para realizar testes em aplicações Spring Boot, incluindo JUnit, Mockito e Spring Test.

#### Utilidade

- **Facilidade na Criação de Testes:** Simplifica a configuração de testes unitários e de integração.

- **Suporte a Testes de Componentes Spring:** Permite testar facilmente componentes do Spring.

#### Exemplo de Uso

**8.1. Teste Unitário:**

```java
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUsers() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk());
    }
}
```

---

### 9. Spring Security Test

#### Teoria
O Spring Security Test fornece funcionalidades para testar a segurança de aplicações Spring, permitindo simular usuários autenticados e verificar o controle de acesso.

#### Utilidade

- **Testes de Segurança Simplificados:** Facilita a criação de testes que verificam a segurança da aplicação.

- **Simulação de Usuários:** Permite simular usuários com diferentes roles e permissões.

#### Exemplo de Uso

**9.1. Teste de Segurança:**

```java
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser // Simula um usuário autenticado
    public void testGetUsersWithAuthentication() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetUsersWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isUnauthorized());
    }
}
```

---

### 10. Auth0 Java JWT

#### Teoria
O Auth0 Java JWT é uma biblioteca que facilita a manipulação de tokens JWT (JSON Web Tokens) em aplicações Java. Os JWTs são amplamente utilizados para autenticação e troca de informações seguras entre partes.

#### Utilidade

- **Geração de Tokens:** Permite criar tokens JWT para autenticar usuários.

- **Validação de Tokens:** Fornece funcionalidades para verificar a autenticidade e integridade dos tokens.

- **Manipulação de Claims:** Facilita a inclusão e leitura de claims no token, que são informações adicionais.

#### Exemplo de Uso

**10.1. Geração e Validação de JWT:**

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {
    private static final String SECRET = "mySecretKey";

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
```

---

### 11. Jackson Databind

#### Teoria
Jackson Databind é uma biblioteca do projeto Jackson que fornece funcionalidades para serialização e desserialização de objetos Java para JSON e vice-versa. É amplamente utilizada em aplicações que precisam interagir com APIs RESTful.

#### Utilidade

- **Serialização:** Converte objetos Java em strings JSON.

- **Desserialização:** Converte strings JSON em objetos Java.

- **Suporte a Anotações:** Permite personalizar a serialização e desserialização com anotações.

#### Exemplo de Uso

**11.1. Serialização e Desserialização:**

```java
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonExample {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String serialize(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    public <T> T deserialize(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }
}
```

---

### 12. Springdoc OpenAPI

#### Teoria
Springdoc OpenAPI é uma biblioteca que gera documentação automática para APIs REST baseadas em Spring. Ele utiliza a especificação OpenAPI para descrever os endpoints de uma aplicação, facilitando a integração e uso da API.

#### Utilidade

- **Documentação Automática:** Gera documentação em tempo real a partir das anotações nas classes.

- **Interface Interativa:** Proporciona uma interface gráfica para testar os endpoints da API.

- **Suporte a Versionamento:** Facilita o gerenciamento de versões da API.

#### Exemplo de Uso

**12.1. Configuração do Springdoc:**

```java
import org.springdoc.core.annotations.SpringDocConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringDocConfig
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Example")
                        .version("1.0")
                        .description("Documentação da API de exemplo"));
    }
}
```
