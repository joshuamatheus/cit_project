# Maven Monorepo

Este projeto é um monorepo baseado em Maven que contém múltiplos módulos para um aplicativo Spring Boot. A estrutura do projeto é organizada em módulos para microserviços e bibliotecas compartilhadas, facilitando a manutenção e o desenvolvimento escalável.

## Estrutura dos Módulos

A estrutura do projeto é organizada da seguinte forma:

- **monorepo**: Módulo raíz do projeto que define a configuração global e os módulos principais.
- **microservices**: Módulo que contém os microserviços:
  - **usermanagement**: Gerenciamento de usuários.
  - **feedbackrequest**: Solicitação de feedback.
  - **feedbackresponse**: Resposta de feedback.
- **libs**: Módulo que contém bibliotecas compartilhadas:
  - **security**: Implementações relacionadas à segurança.

## Configuração do Projeto (POM)

### monorepo/pom.xml

Este é o arquivo `pom.xml` principal que define o projeto como um módulo pai (parent) de outros módulos.

### microservices/pom.xml

Define o módulo `microservices` e suas dependências, incluindo Spring Boot, JPA, e outras bibliotecas necessárias.

### libs/pom.xml

Define o módulo `libs` que contém módulos de bibliotecas compartilhadas como `security`.

### Exemplos de Uso

### Criando um Endpoint no Spring Boot

Para criar um endpoint, você pode adicionar um controlador ao módulo de microserviço desejado. Exemplo:

```java
package com.ciandt.nextgen25.usermanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    public String getUsers() {
        return "Lista de usuários";
    }
}
```

### Acessando o Banco de Dados

O projeto está configurado para usar JPA. Para acessar o banco de dados, crie uma entidade e um repositório. Exemplo:

```java
package com.ciandt.nextgen25.usermanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private Long id;
    private String name;

    // Getters e Setters
}
```

```java
package com.ciandt.nextgen25.usermanagement.repository;

import com.ciandt.nextgen25.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

### Criando Testes com TDD

Para criar testes, utilize o framework JUnit. Exemplo de um teste simples:

```java
package com.ciandt.nextgen25.usermanagement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser() throws Exception {
        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"John Doe\"}")
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
```

## Glossário

- **Maven**: Ferramenta de automação de compilação e gerenciamento de dependências para projetos Java.
- **POM (Project Object Model)**: Arquivo XML que contém informações sobre o projeto e configurações para o Maven.
- **Spring Boot**: Framework Java para criação de aplicativos independentes em produção.
- **Microserviços**: Arquitetura que estrutura uma aplicação como um conjunto de serviços pequenos e independentes.
- **JPA (Java Persistence API)**: API para gerenciamento de dados relacionais no Java.
- **JUnit**: Framework para criação e execução de testes automatizados em Java.
- **TDD (Test-Driven Development)**: Metodologia de desenvolvimento de software em que os testes são escritos antes do código funcional.