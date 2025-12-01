# Domain Driven Design

## Padrões Estratégicos e Padrões Táticos

Os padrões táticos e estratégicos no Domain-Driven Design (DDD) são componentes essenciais que ajudam a estruturar sistemas complexos de forma eficaz. Os **padrões estratégicos** fornecem uma visão de alto nível, ajudando a identificar e organizar diferentes partes do domínio em contextos delimitados, além de definir como essas partes interagem entre si. Essa abordagem facilita a colaboração entre equipes e o gerenciamento da complexidade do sistema. Por outro lado, os **padrões táticos** se concentram nos detalhes da implementação dentro de cada contexto delimitado, abordando a modelagem de elementos como entidades, objetos de valor, agregados e serviços. Enquanto os padrões estratégicos orientam a organização e a estrutura geral do sistema, os padrões táticos garantem que a lógica de negócios e a arquitetura do código estejam alinhadas com as necessidades específicas do domínio, permitindo uma construção mais robusta e adaptável.

```plantuml
!include <edgy/edgy>

$organisationFacet("Estratégico") {
   $organisation("Contexto Delimitado\n(Bounded Contexts)")
   $organisation("Modelo de Domínio\n(Domain Models)")
   $organisation("Subdomínios\n(Subdomains)")
   $organisation("Mapa de Contextos\n(Context Mapping)")
   $organisation("Anticorrupção\n(Anti-Corruption Layer)")
}

$architectureFacet("Tático") {
   $capability("Entidade\n(Entity)")
   $capability("Objeto de Valor\n(Value Object)")
   $capability("Agregado\n(Aggregates)")
   $capability("Repositório\n(Repository)")
   $capability("Serviço\n(Service)")
   $capability("Fábrica\n(Factory)")
   $capability("Evento de Domínio\n(Domain Event)")
}
```

## Padrões Estratégicos

1. **Contexto Delimitado (Bounded Context)**:
      - Um contexto delimitado é uma parte específica do domínio onde um modelo particular é aplicado. Este padrão ajuda a dividir um sistema complexo em partes gerenciáveis, cada uma com sua própria linguagem e regras. A interação entre diferentes contextos delimitados deve ser claramente definida, evitando confusão e conflitos de modelo.

2. **Modelo de Domínio (Domain Model)**:
      - O modelo de domínio representa a estrutura e a lógica de negócios do sistema. Ele captura as regras e conceitos fundamentais do domínio, permitindo que as equipes compreendam e trabalhem com os aspectos mais importantes do negócio. Um modelo de domínio bem projetado é essencial para a eficácia do DDD.

3. **Integração entre Contextos (Context Mapping)**:
      - Este padrão trata da interação entre diferentes contextos delimitados. O mapeamento de contextos ajuda a documentar e entender como os contextos se comunicam, identificando as relações e as dependências entre eles. Isso é crucial para garantir que a integração entre diferentes partes do sistema seja feita de maneira eficiente e sem problemas.

4. **Anticorrupção (Anti-Corruption Layer)**:
      - O padrão de anticorrupção é utilizado para proteger um contexto delimitado de influências externas que podem corromper seu modelo. Ele serve como uma camada intermediária que transforma as comunicações e dados de sistemas externos, garantindo que as regras e a integridade do modelo do domínio não sejam comprometidas.

5. **Subdomínios (Subdomains)**:
      - Os subdomínios são partes do domínio principal que podem ser abordadas de forma independente. Eles ajudam a identificar áreas específicas de foco dentro do sistema, permitindo que as equipes se concentrem em resolver problemas específicos. Os subdomínios podem ser classificados como núcleo (Core Domain), de suporte (Supporting Subdomain) ou genérico (Generic Subdomain), dependendo de sua importância e complexidade dentro do sistema.

6. **Desenho de Sistema (System Design)**:
      - Este padrão envolve a definição da arquitetura e da estrutura do sistema, incluindo a escolha de tecnologias, frameworks e padrões de comunicação. Um bom desenho de sistema leva em consideração os contextos delimitados e a integração entre eles, garantindo que o sistema seja escalável, manutenível e alinhado com os objetivos do negócio.

## Padrões Táticos

1. **Entidade (Entity)**:
      - Entidades são objetos que possuem uma identidade única e representam conceitos do domínio que mudam ao longo do tempo. Elas são definidas por sua identidade, não apenas por seus atributos, e são responsáveis por manter seu próprio estado e comportamento, como um usuário ou um produto.

2. **Objeto de Valor (Value Object)**:
      - Objetos de valor são objetos que não possuem identidade própria e são definidos apenas por seus atributos. Eles são imutáveis e podem ser trocados entre instâncias sem afetar a lógica do domínio. Exemplos incluem um endereço ou uma data. O uso de objetos de valor ajuda a encapsular conceitos que não precisam de identidade única.

3. **Agregado (Aggregate)**:
      - Um agregado é um grupo de entidades e objetos de valor que são tratados como uma unidade única para fins de consistência de dados. Cada agregado tem uma raiz (aggregate root) que controla o acesso a seus membros internos, garantindo que as regras de negócios sejam seguidas. Isso ajuda a manter a integridade dos dados e a simplificar as operações.

4. **Repositório (Repository)**:
      - Um repositório é uma abstração que fornece uma interface para acessar e manipular agregados e entidades. Ele oculta os detalhes de persistência de dados, permitindo que os desenvolvedores se concentrem na lógica de negócios. Os repositórios facilitam operações como adicionar, buscar e remover entidades, proporcionando uma maneira organizada de interagir com a camada de dados.

5. **Serviço (Service)**:
      - Serviços são utilizados para encapsular a lógica de negócios que não se encaixa naturalmente em uma entidade ou objeto de valor. Eles podem realizar operações complexas que envolvem múltiplas entidades ou agregados, como cálculos ou manipulações de dados. Serviços ajudam a manter a lógica de negócios organizada e isolada, promovendo a reutilização e a testabilidade.

6. **Fábrica (Factory)**:
      - Fábricas são responsáveis pela criação de entidades e agregados, especialmente quando a criação envolve lógica complexa ou múltiplas etapas. Elas abstraem o processo de construção, garantindo que os objetos sejam criados em um estado válido e consistente. O uso de fábricas ajuda a simplificar a criação de objetos e a centralizar a lógica de construção.

7. **Evento de Domínio (Domain Event)**:
      - Eventos de domínio representam algo que ocorreu no sistema e que pode ter impacto em outras partes do aplicativo. Eles são utilizados para comunicar mudanças de estado e podem ser disparados a partir de entidades ou serviços. O uso de eventos de domínio ajuda a manter um sistema reativo e a desacoplar componentes, permitindo que diferentes partes do sistema reajam a mudanças de maneira eficiente.
