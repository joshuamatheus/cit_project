# Aprofundando no Spring boot

O Spring Boot é um framework baseado no ecossistema Spring, que visa simplificar o desenvolvimento de aplicações Java. Sua filosofia é proporcionar uma experiência de desenvolvimento rápida, produtiva e de fácil configuração, permitindo que os desenvolvedores se concentrem nas funcionalidades do software, em vez de se preocupar com a configuração de infraestrutura.

## Filosofia do Spring Boot

O Spring Boot se destaca por sua filosofia de facilitar o desenvolvimento de aplicações Java, aderindo a práticas recomendadas como DDD e SOLID. Além disso, sua abordagem de configuração automática e suporte a AOP tornam o desenvolvimento mais eficiente e organizado. Isso permite que os desenvolvedores se concentrem nas características específicas do domínio e no design da aplicação, ao invés de se perderem em detalhes de configuração e infraestrutura. Além disso, o conceito de beans permite uma gestão eficiente e flexível dos componentes da aplicação.


## Principais Características do Spring Boot:
- **Configuração Automática:** O Spring Boot automaticamente configura a aplicação com base nas dependências que você adiciona, reduzindo a necessidade de configuração manual.
- **Standalone:** As aplicações Spring Boot podem ser executadas como aplicações Java independentes, sem a necessidade de um servidor de aplicativos externo, pois incluem um servidor embutido (como Tomcat).
- **Pronto para Produção:** Inclui recursos de monitoramento e gerenciamento prontos para produção, como métricas e health checks.

## Beans  

Um **bean** é um objeto que é instanciado, configurado e gerenciado pelo contêiner de Injeção de Dependência (IoC) do Spring Framework. A definição de um bean é fundamental para a forma como o Spring Boot lida com a configuração e a gestão de objetos em uma aplicação.

### Características dos Beans
1. **Instância Gerenciada:** O Spring se encarrega de criar e gerenciar o ciclo de vida dos beans.
2. **Configuração:** Beans podem ser configurados através de anotações ou arquivos de configuração XML.
3. **Injeção de Dependência:** O Spring permite que os beans sejam injetados em outros beans, facilitando a construção de aplicações desacopladas.
4. **Escopo:** Beans podem ter diferentes escopos, como singleton (uma única instância por contêiner) ou prototype (uma nova instância a cada solicitação).

### Como Definir um Bean
Existem diversas maneiras de definir um bean no Spring:

1. **Usando Anotações:**
   - `@Component`: Indica que a classe é um bean genérico.
   - `@Service`: Usada para classes de serviço.
   - `@Repository`: Usada para classes de acesso a dados.
   - `@Controller`: Usada para classes de controle em aplicações web.

**Exemplo:**

```java
import org.springframework.stereotype.Component;

@Component
public class MyBean {
    public void doSomething() {
        System.out.println("Fazendo algo!");
    }
}
```

2. **Usando o Método de Fábrica:**
   - Você também pode definir um bean em um método que retorna uma instância do objeto desejado, anotando o método com `@Bean`.

**Exemplo:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }
}
```

### Uso de Beans na Aplicação

Os beans podem ser injetados em outros componentes do Spring, como serviços e controladores, utilizando a injeção de dependência por meio da anotação `@Autowired`.

**Exemplo de Injeção de Dependência:**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    
    @Autowired
    private MyBean myBean;

    public void performAction() {
        myBean.doSomething();
    }
}
```

## Teoria de Operação do Spring Boot

O Spring Boot opera com base no conceito de configuração automática e convenções sobre configurações. Quando uma aplicação Spring Boot é iniciada, o framework examina as dependências presentes no classpath e configura automaticamente os beans necessários.

### Ciclo de Vida da Aplicação
1. **Inicialização:** O Spring Boot inicializa o contexto da aplicação e carrega todos os beans definidos.
2. **Configuração Automática:** O Spring Boot aplica a configuração automática com base nas dependências encontradas, como a configuração de um banco de dados quando `spring-boot-starter-data-jpa` está presente.
3. **Injeção de Dependência:** A injeção de dependência é realizada, permitindo que os componentes colaborem entre si.
4. **Execução da Aplicação:** A aplicação começa a responder a requisições, utilizando os controladores e serviços definidos.

## Programação Orientada a Aspectos (AOP)

A Programação Orientada a Aspectos (AOP) é uma técnica que permite modularizar preocupações transversais, como logging, segurança e gerenciamento de transações, separando-as da lógica de negócio principal.

## Como o Spring Abraça AOP

O Spring Framework fornece suporte robusto para AOP, permitindo que você defina aspectos e aplique lógica de maneira declarativa.

**Principais Conceitos:**

- **Aspecto:** Um módulo que contém preocupações transversais.

- **Join Point:** Um ponto na execução da aplicação, como a chamada de um método.

- **Advice:** A ação a ser executada em um Join Point.

- **Pointcut:** Uma expressão que define onde o Advice deve ser aplicado.

### Exemplo de AOP no Spring Boot

**Definindo um Aspecto:**

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.service.*.*(..))") // Aplica antes da execução de qualquer método nos serviços
    public void logBeforeMethod() {
        System.out.println("Método chamado...");
    }
}
```

### Uso de AOP

Ao utilizar AOP, você pode facilmente adicionar logging, segurança ou outras funcionalidades sem modificar o código existente nas classes de serviço:

```java
@Service
public class OrderServiceImpl implements OrderService {
    public void placeOrder(Order order) {
        // lógica de negócio
        orderRepository.save(order);
    }
}
```

O `logBeforeMethod` será chamado automaticamente antes de qualquer método no pacote `com.example.service`, permitindo que você insira lógica de logging de maneira limpa e não intrusiva.