# Docker

Docker é uma plataforma de software que permite a criação, o envio e a execução de aplicações em contêineres. Os contêineres são unidades leves e portáteis que podem ser executadas em qualquer ambiente que tenha o Docker instalado, garantindo que a aplicação funcione da mesma forma, independentemente do sistema operacional subjacente.

## Conceitos Fundamentais

### 1. Contêineres

- **Definição**: Um contêiner é uma instância de uma imagem Docker em execução. Ele encapsula tudo o que uma aplicação precisa para rodar: código, bibliotecas, dependências e configurações.
- **Isolamento**: Contêineres compartilham o mesmo núcleo do sistema operacional, mas são isolados entre si e do host, permitindo que várias aplicações sejam executadas de forma segura e eficaz no mesmo sistema.

### 2. Imagens

- **Definição**: Uma imagem Docker é uma construção estática que contém todos os arquivos necessários para executar uma aplicação. Ela é composta de camadas, onde cada camada representa uma alteração em relação à camada anterior.
- **Criação**: As imagens são geralmente criadas através de um arquivo chamado `Dockerfile`, que contém instruções sobre como construir a imagem.

### 3. Dockerfile

- **Definição**: Um Dockerfile é um script de texto que contém uma série de comandos que o Docker utiliza para construir uma imagem.
- **Exemplo de Comandos**:
  - `FROM`: Define a imagem base.
  - `RUN`: Executa comandos durante a construção da imagem.
  - `COPY`: Copia arquivos do sistema de arquivos do host para a imagem.
  - `CMD`: Define o comando padrão a ser executado quando um contêiner é iniciado a partir da imagem.

### 4. Docker Hub

- **Definição**: O Docker Hub é um repositório público onde os usuários podem armazenar e compartilhar imagens Docker.
- **Uso**: As imagens podem ser baixadas do Docker Hub ou de repositórios privados, permitindo fácil acesso a imagens pré-construídas e compartilhamento de suas próprias imagens.

## Vantagens do Docker

### 1. Portabilidade

- As aplicações em contêineres podem ser executadas em qualquer lugar que suporte Docker, eliminando problemas de "funciona na minha máquina".

### 2. Eficiência

- Os contêineres são mais leves que máquinas virtuais, pois compartilham o núcleo do sistema operacional. Isso resulta em menor consumo de recursos e tempos de inicialização mais rápidos.

### 3. Isolamento

- Cada contêiner é isolado, o que significa que as aplicações podem operar em ambientes diferentes sem conflitos de dependências ou configurações.

### 4. Escalabilidade

- Docker facilita a escalabilidade horizontal, permitindo que múltiplas instâncias de um contêiner sejam executadas em paralelo para atender a picos de demanda.

### 5. DevOps e CI/CD

- Docker se integra bem com práticas de DevOps e pipelines de integração contínua/entrega contínua (CI/CD), permitindo que desenvolvedores e operadores trabalhem juntos de maneira mais eficaz.

## Casos de Uso Comuns

1. **Desenvolvimento Local**: Desenvolvedores podem usar Docker para criar ambientes de desenvolvimento consistentes e replicáveis.
2. **Microservices**: Docker é frequentemente usado em arquiteturas de microservices, onde cada serviço pode ser executado em seu próprio contêiner.
3. **Testes Automatizados**: É possível configurar ambientes de testes que podem ser criados e destruídos rapidamente, garantindo que os testes sejam executados em condições controladas.
4. **Implantação em Nuvem**: Docker facilita a implantação de aplicações em ambientes de nuvem, como AWS, Google Cloud e Azure, onde contêineres podem ser gerenciados e orquestrados de forma eficaz.

## Orquestração de Contêineres

Para gerenciar múltiplos contêineres, ferramentas de orquestração como **Kubernetes** e **Docker Swarm** são frequentemente utilizadas. Essas ferramentas permitem a automação do deployment, escalabilidade e gerenciamento de contêineres em larga escala.


# Docker Compose

Docker Compose é uma ferramenta que permite definir e gerenciar aplicações multi-contêiner de forma simples e eficiente. Com o Docker Compose, você pode usar um arquivo YAML para configurar todos os serviços necessários para sua aplicação, facilitando o gerenciamento e a orquestração de contêineres.

## Conceitos Fundamentais

### 1. Definição

- **Docker Compose** é uma ferramenta que permite definir e executar aplicações Docker compostas por múltiplos contêineres. Ele utiliza um arquivo de configuração (geralmente chamado de `docker-compose.yml`) onde você descreve os serviços que compõem sua aplicação.

### 2. Arquivo `docker-compose.yml`

- O arquivo `docker-compose.yml` é onde você define todos os serviços, redes e volumes necessários para sua aplicação. A sintaxe é baseada em YAML, o que torna a configuração bastante legível.

#### Estrutura Básica do `docker-compose.yml`

```yaml
version: '3'  # Versão do Compose file format
services:     # Definição dos serviços
  web:        # Nome do serviço
    image: nginx:latest  # Imagem a ser utilizada
    ports:
      - "80:80"  # Mapeamento de portas
  db:         # Outro serviço (banco de dados)
    image: postgres:latest
    environment:  # Variáveis de ambiente
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
```

## Principais Componentes

### 1. Serviços

- Cada serviço no `docker-compose.yml` representa um contêiner. Você pode especificar a imagem a ser usada, configurações de rede, variáveis de ambiente, volumes e muito mais.

### 2. Redes

- O Compose cria uma rede padrão para os serviços se comunicarem entre si. Você pode personalizar redes, permitindo que serviços em diferentes redes se comuniquem ou isolem serviços conforme necessário.

### 3. Volumes

- Volumes permitem que você persista dados gerados por contêineres. Eles são armazenados fora do ciclo de vida do contêiner, garantindo que os dados não sejam perdidos quando um contêiner é removido ou atualizado.

## Comandos Principais do Docker Compose

### 1. `docker-compose up`

- Este comando cria e inicia todos os serviços definidos no arquivo `docker-compose.yml`.
- Exemplo: 
  ```bash
  docker-compose up
  ```

### 2. `docker-compose down`

- Para de todos os serviços e remove os contêineres, redes e volumes associados.
- Exemplo:
  ```bash
  docker-compose down
  ```

### 3. `docker-compose build`

- Este comando constrói ou reconstrói os serviços definidos no Compose file.
- Exemplo:
  ```bash
  docker-compose build
  ```

### 4. `docker-compose logs`

- Mostra os logs de todos os serviços em execução, permitindo que você monitore a saída dos contêineres.
- Exemplo:
  ```bash
  docker-compose logs
  ```

### 5. `docker-compose exec`

- Permite que você execute comandos em um contêiner em execução.
- Exemplo:
  ```bash
  docker-compose exec web bash
  ```

## Vantagens do Docker Compose

### 1. Simplicidade

- O Docker Compose simplifica o gerenciamento de aplicações multi-contêiner, permitindo que você defina todos os serviços em um único arquivo de configuração.

### 2. Consistência

- A configuração da aplicação pode ser facilmente compartilhada entre diferentes ambientes (desenvolvimento, teste, produção), garantindo que todos estejam usando a mesma configuração.

### 3. Facilidade de Uso

- Docker Compose fornece uma interface de linha de comando intuitiva que torna fácil iniciar, parar e gerenciar aplicações complexas.

### 4. Orquestração

- Embora o Docker Compose não seja uma ferramenta de orquestração completa como o Kubernetes, ele fornece funcionalidades básicas de orquestração para aplicações que não exigem a complexidade de uma solução mais robusta.
