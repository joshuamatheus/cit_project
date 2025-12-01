# Maven

O **Maven** é uma ferramenta de automação de construção e gerenciamento de projetos em Java. Ele fornece uma maneira padrão de construir, testar e gerenciar dependências em projetos Java. O Maven tem como principal objetivo simplificar o processo de construção de projetos e gerenciar as bibliotecas utilizadas.

## Teoria

Maven utiliza o conceito de **Project Object Model (POM)**, que é um arquivo XML (`pom.xml`) que contém informações sobre o projeto e configurações sobre como construir o projeto. O POM é o coração do Maven e define as dependências, plugins, configurações de construção e outras informações relevantes para o projeto.

## Utilidade do Maven

1. **Gerenciamento de Dependências:** O Maven permite que você declare as bibliotecas que seu projeto precisa. Ele baixa automaticamente essas bibliotecas e suas dependências transitivas a partir de repositórios remotos.

2. **Construção do Projeto:** O Maven fornece um conjunto de comandos para compilar, testar e empacotar seu projeto de maneira consistente. O processo de construção é definido no arquivo POM.

3. **Estrutura de Projeto Padrão:** O Maven impõe uma estrutura de diretórios padrão para projetos, facilitando a organização do código e a colaboração entre desenvolvedores.

4. **Plugins:** O Maven possui uma extensa coleção de plugins que permitem executar tarefas como teste, empacotamento e implantação.

5. **Integração Contínua:** O Maven se integra facilmente com ferramentas de CI/CD (Integração Contínua/Entrega Contínua), permitindo que você automatize o processo de construção e testes.

## Exemplos de Uso do Arquivo POM

O arquivo `pom.xml` é o coração do Maven. Aqui estão algumas seções comuns que você encontrará em um arquivo POM.

### Exemplo Básico de um `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <dependencies>
        <!-- Dependência do Spring Boot Starter Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.5.4</version>
        </dependency>
        <!-- Dependência do JUnit para testes -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Plugin do Maven Compiler -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Explicação do POM:

- **groupId:** Identifica o grupo ou organização ao qual o projeto pertence.

- **artifactId:** Nome único do projeto.

- **version:** Versão do projeto.

- **dependencies:** Declara as bibliotecas que o projeto requer. O Maven irá baixar essas dependências automaticamente.

- **build:** Define a configuração de construção do projeto, incluindo plugins que serão utilizados, como o `maven-compiler-plugin` para compilar o código.

## Exemplos de Uso da Linha de Comando

O Maven fornece uma série de comandos que você pode executar no terminal para gerenciar o ciclo de vida do projeto. Aqui estão alguns comandos comuns:

### 1. Criar um Novo Projeto

```bash
mvn archetype:generate -DgroupId=com.example -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### 2. Compilar o Projeto

```bash
cd my-app
mvn compile
```

### 3. Executar Testes

```bash
mvn test
```

### 4. Empacotar o Projeto

```bash
mvn package
```

Este comando compila o código e empacota o projeto em um arquivo `.jar` ou `.war`, dependendo do tipo de empacotamento definido no POM.

### 5. Executar o Projeto

Se você estiver usando o Spring Boot, você pode executar o projeto diretamente com o Maven:

```bash
mvn spring-boot:run
```

### 6. Limpar o Projeto

Para limpar os arquivos gerados durante a construção, como classes compiladas e artefatos de empacotamento, utilize:

```bash
mvn clean
```

## Conclusão

O Maven é uma ferramenta poderosa que simplifica o gerenciamento de projetos Java, oferecendo recursos para gerenciamento de dependências, construção, teste e empacotamento. Através do uso do arquivo `pom.xml` e da linha de comando, os desenvolvedores podem automatizar o ciclo de vida de seus projetos, garantindo consistência e eficiência no desenvolvimento.
