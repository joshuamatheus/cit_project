# Conceitos Chave Para o Desenvolvimento

## 1. Metodologias de Desenvolvimento de Software
### Agile & Scrum

- **Agile**: Uma abordagem iterativa e incremental que prioriza a flexibilidade e a colaboração. As equipes trabalham em sprints, que são ciclos curtos de desenvolvimento, permitindo ajustes rápidos com base no feedback.
- **Scrum**: Um framework dentro da metodologia Agile que organiza o trabalho em sprints, com papéis definidos (Scrum Master, Product Owner, e a equipe de desenvolvimento) e eventos como reuniões diárias e revisões de sprint.

![type:video](https://www.youtube.com/embed/cT_X4_n0NJ4?si=mnAU5hUZEeb9hWrF)

## 2. Princípios Arquiteturais
### SOLID

- Uma série de princípios de design que visam tornar os sistemas mais compreensíveis e fáceis de manter. Inclui princípios como Responsabilidade Única (Single Responsibility), Abertura/Fechamento (Open/Closed), e Substituição de Liskov (Liskov Substitution).

![type:video](https://www.youtube.com/embed/6SfrO3D4dHM?si=dbMwF21ahYjgpmlC)

### Domain-Driven Design (DDD)

- Uma abordagem que foca na colaboração entre especialistas de domínio e desenvolvedores para criar modelos que refletem a complexidade do negócio. Inclui conceitos como subdomínios, contextos delimitados, entidades, objetos de valor, agregados, repositórios, serviços, eventos de domínio e fábricas.

![type:video](https://www.youtube.com/embed/Lw3bDBVjZMo?si=X1-5xmfvOt1vs0I5)
![type:video](https://www.youtube.com/embed/2SeL8i9eFCY?si=RFvFOdaqLSEcaH75)

### Microservices

- Uma arquitetura que divide aplicações em serviços pequenos e independentes, cada um implementando uma funcionalidade específica. Isso permite escalabilidade, facilidade de manutenção e a capacidade de implementar tecnologias diferentes para cada serviço.

![type:video](https://www.youtube.com/embed/eN8dFfTrEtQ?si=pyp3Pc79HTZQh70Q)

## 3. Frameworks e Tecnologias

### Técnicas de Engenharia

- **TDD (Test-Driven Development)**: é uma prática de desenvolvimento de software que enfatiza a criação de testes automatizados antes da implementação do código. O processo envolve três etapas principais: escrever um teste que define uma nova funcionalidade (que inicialmente falhará), implementar o código necessário para passar nesse teste e, em seguida, refatorar o código para melhorar sua estrutura sem alterar seu comportamento. Essa abordagem ajuda a garantir que o código atenda aos requisitos especificados, melhora a qualidade do software e facilita a manutenção a longo prazo, promovendo uma cultura de desenvolvimento mais orientada a testes.

![type:video](https://www.youtube.com/embed/hwgy0l7_XRE?si=IIDnmBwusQ2nHCBN)

### Protocolos

- **HTTP**: HTTP (Hypertext Transfer Protocol) é um protocolo de comunicação utilizado na web para a transferência de informações entre clientes (como navegadores) e servidores. Ele opera no modelo cliente-servidor, onde os clientes fazem solicitações usando métodos como GET, POST, PUT e DELETE. O HTTP é um protocolo sem estado, significando que cada solicitação é independente, e sua versão segura, HTTPS, utiliza criptografia para proteger a comunicação. Além disso, as mensagens HTTP podem incluir cabeçalhos que fornecem informações adicionais sobre a solicitação ou resposta, desempenhando um papel fundamental na operação de aplicações web e APIs.

![type:video](https://www.youtube.com/embed/PcHbyGVoqZk?si=NnZYEdlxu_0vH9Ox)

### Frontend

- **Next.js**: Um framework para React que permite renderização do lado do servidor (SSR) e geração de sites estáticos, facilitando a construção de interfaces de usuário rápidas e otimizadas.
- **React**: Uma biblioteca JavaScript para construção de interfaces de usuário, permitindo a criação de componentes reutilizáveis e a gestão eficiente do estado da aplicação.

![type:video](https://www.youtube.com/embed/aJR7f45dBNs?si=NMthNRAfWHtGA6p8)

### Backend

- **Spring Boot**: Um framework Java que simplifica a configuração e o desenvolvimento de aplicações, permitindo a criação rápida de serviços robustos e escaláveis.

![type:video](https://www.youtube.com/embed/Nbtzy7o3pPg?si=xJ30M8x7vSGxKuqF)

- **Express**: Um framework minimalista para Node.js que facilita a criação de aplicações web e APIs, proporcionando um conjunto flexível de recursos.

![type:video](https://www.youtube.com/embed/FcKt_UL3iBc?si=3Le_78IYnvP_zwcQ)

- **FastAPI**: Um framework moderno para a construção de APIs com Python, que oferece alta performance e suporte a validação automática de dados.

![type:video](https://www.youtube.com/embed/R26iojTwUv8?si=OqNEQc8QTM5AYJSV)

## 4. Ferramentas de Desenvolvimento
### Docker

- Uma plataforma que permite a criação, implantação e execução de aplicações em contêineres. Isso garante que as aplicações funcionem de maneira consistente em diferentes ambientes, desde o desenvolvimento até a produção.

![type:video](https://www.youtube.com/embed/01MR38eDXz8?si=0HxYIKgjheUGqoOz)

### Maven

- Uma ferramenta de gerenciamento de projetos para Java que facilita a construção, dependência e gerenciamento de projetos. O Maven utiliza um arquivo POM (Project Object Model) para descrever a estrutura do projeto e suas dependências.

### npm

- O gerenciador de pacotes padrão para Node.js, que permite a instalação e gerenciamento de bibliotecas e dependências necessárias para projetos JavaScript.

### Jest

- Um framework de testes para JavaScript que foca na simplicidade e na eficiência, permitindo a realização de testes unitários e de integração de maneira fácil e rápida.
