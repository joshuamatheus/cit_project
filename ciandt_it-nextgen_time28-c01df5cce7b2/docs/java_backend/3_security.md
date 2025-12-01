# Documentação do Sistema de Segurança com JWT

Este projeto implementa um sistema de segurança utilizando JWT (JSON Web Tokens) para autenticação e autorização em uma aplicação Spring. Esta documentação fornece uma visão geral do comportamento do código e exemplos de como utilizá-lo.

## Estrutura do Projeto

### Configurações de Segurança

- **AbstractSecurityConfig**: Classe abstrata que define a configuração base de segurança. Desabilita acessos anônimos, CSRF e define a política de sessão como STATELESS. Adiciona um filtro de autenticação antes do `BasicAuthenticationFilter`.

- **JwtSecurityConfig**: Extende `AbstractSecurityConfig` e fornece um filtro de autenticação JWT, `UserJWTAuthenticationFilter`.

- **UserClaimSecurityConfig**: Extende `AbstractSecurityConfig` e fornece um filtro de autenticação baseado em claims de usuário, `UserClaimsAuthenticationFilter`.

### Entidades de Usuário

- **LoggedUser**: Implementa `UserInterface` e representa um usuário autenticado com informações como ID, nome, email, tipo e PDM. Implementa o método `getAuthorities` para fornecer as autoridades do usuário.

- **UserDetailImpl**: Implementa `UserDetails` do Spring Security e utiliza `UserInterface` para obter detalhes do usuário como username, password e authorities.

- **UserInterface**: Interface que define métodos para obtenção de informações do usuário, como ID, nome, email, tipo, PDM, password e autoridades.

### Filtros de Autenticação

- **UserJWTAuthenticationFilter**: Filtro que autentica usuários com base em um token JWT presente no cabeçalho `Authorization` da requisição HTTP. Atualiza o contexto de segurança do Spring com a autenticação do usuário.

- **UserClaimsAuthenticationFilter**: Filtro que autentica usuários com base em claims de usuário presentes no cabeçalho `x-user` da requisição HTTP.

### Anotações de Segurança

- **EnableJwtSecurity**: Anotação que importa a configuração de segurança JWT (`JwtSecurityConfig`) para uma classe.

- **HasType**: Anotação para verificar se o usuário possui uma autoridade específica antes de acessar um método ou classe.

- **EnableUserClaimsSecurity**: Anotação que importa a configuração de segurança baseada em claims de usuário (`UserClaimSecurityConfig`) para uma classe.

### Serviço de Token JWT

- **JwtTokenService**: Serviço responsável por gerar e verificar tokens JWT usando a biblioteca `auth0.jwt`. Define métodos para gerar tokens com informações do usuário e para extrair informações de usuário a partir de tokens.

## Exemplos de Uso

### Habilitar Segurança JWT

Para habilitar a segurança JWT em uma classe de configuração, use a anotação `@EnableJwtSecurity`.

```java
@EnableJwtSecurity
public class MySecurityConfig {
    // Sua configuração adicional aqui
}
```

### Proteger Métodos ou Classes com Autoridade

Para restringir o acesso a métodos ou classes conforme a autoridade do usuário, utilize a anotação `@HasType`.

```java
@HasType("ROLE_ADMIN")
public void metodoRestrito() {
    // Implementação do método
}
```

### Geração de Token JWT

Utilize o serviço `JwtTokenService` para gerar um token JWT para um usuário.

```java
@Autowired
private JwtTokenService jwtTokenService;

public String gerarTokenParaUsuario(UserInterface usuario) throws Exception {
    return jwtTokenService.generateToken(usuario);
}
```

### Autenticação com JWT

As requisições HTTP devem incluir o token JWT no cabeçalho `Authorization` para que o filtro `UserJWTAuthenticationFilter` possa autenticar o usuário.

```http
GET /api/protected-resource HTTP/1.1
Authorization: Bearer <token_jwt>
```

### Autenticação com Claims de Usuário

Para habilitar a segurança com claims de usuário em uma classe de configuração, use a anotação `@EnableUserClaimsSecurity`.

```java
@EnableUserClaimsSecurity
public class MySecurityConfig {
    // Sua configuração adicional aqui
}
```

As requisições HTTP devem incluir os claims de usuário no cabeçalho `x-user` para que o filtro `UserClaimsAuthenticationFilter` possa autenticar o usuário.

```http
GET /api/protected-resource HTTP/1.1
x-user: <dados_do_usuario>
```

## Glossário

- **JWT (JSON Web Token)**: Um padrão aberto para criação de tokens de acesso baseados em JSON que permitem a transmissão de informações verificáveis entre partes.

- **CSRF (Cross-Site Request Forgery)**: Um tipo de ataque de segurança contra aplicações web, onde o usuário é induzido a executar ações não desejadas em uma aplicação na qual está autenticado.

- **Stateless**: Uma política de sessão onde o servidor não armazena nenhuma informação sobre as sessões do usuário. Cada requisição é independente e carrega suas próprias credenciais.

- **`OncePerRequestFilter`**: Classe base do Spring para filtros que garantem que a execução ocorra uma única vez por requisição.

- **`SecurityContextHolder`**: Classe do Spring Security que armazena o contexto de segurança, incluindo detalhes de autenticação.

- **HMAC SHA256**: Um tipo de algoritmo de hash criptográfico que combina uma chave secreta com uma função de hash para gerar uma assinatura segura.

- **`UserDetails`**: Interface do Spring Security que representa as informações principais de um usuário.

- **`GrantedAuthority`**: Representa uma autoridade concedida a um usuário, usada para restrições de acesso.

- **`@EnableMethodSecurity`**: Anotação do Spring Security que habilita a segurança baseada em anotações nos métodos.
