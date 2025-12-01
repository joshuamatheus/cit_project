# Microservices: Abordagem no Projeto

Microservices são uma abordagem arquitetônica que estrutura uma aplicação como um conjunto de pequenos serviços independentes, cada um executando uma função específica dentro do sistema. Esses serviços podem ser desenvolvidos, implantados e escalados de forma independente, promovendo agilidade e flexibilidade no desenvolvimento de software.

## Decomposição em Microservices

Um dos aspectos-chave da arquitetura de microservices é a decomposição do sistema em serviços menores, que são organizados em torno de **contextos delimitados** (ou **bounded contexts**). Esta abordagem é inspirada nos princípios da Domain-Driven Design (DDD) e permite que cada microservice represente um subdomínio específico da aplicação.

### Contexto Delimitado

- **Definição**: Um contexto delimitado define os limites dentro dos quais um modelo de domínio é aplicado. Isso significa que cada microservice tem sua própria lógica de negócios e modelo de dados, que não interfere com outros serviços.
- **Benefícios**:
  - **Isolamento**: Alterações em um serviço não afetam diretamente outros serviços.
  - **Clareza**: Cada equipe pode se concentrar em um subdomínio específico, facilitando a compreensão e o desenvolvimento.
  - **Escalabilidade**: Serviços podem ser escalados de acordo com a demanda do seu subdomínio específico.

## API Gateway

Para gerenciar a comunicação entre microservices, muitas vezes é utilizado um **API Gateway**. Este padrão atua como um ponto de entrada único para todas as requisições externas, que redireciona as chamadas para os serviços apropriados. 

### Vantagens do API Gateway:

- **Abstração**: Esconde a complexidade da comunicação entre os serviços.
- **Segurança**: Pode implementar autenticação e autorização centralizadas.
- **Monitoramento**: Facilita o rastreamento e a análise de desempenho das requisições.

## Uso de uma Base de Dados Comum

Embora cada microservice deva idealmente ter sua própria base de dados para garantir o isolamento de dados, pode haver justificativas para o uso de uma **base de dados compartilhada**:

- **Simplicidade**: Para sistemas menores ou em fases iniciais, o uso de uma única base de dados pode simplificar a arquitetura e reduzir a complexidade.
- **Evitar Mensageria**: A utilização de uma base de dados comum pode eliminar a necessidade de um sistema de mensageria para sincronizar dados entre serviços, o que pode ser vantajoso em termos de desempenho e redução de latência nas operações.

### Considerações:

Enquanto essa abordagem pode ser vantajosa em cenários específicos, é importante ser cauteloso, pois a dependência de uma base de dados comum pode levar à introdução de acoplamento entre microservices, o que contradiz um dos princípios fundamentais da arquitetura de microservices.

## Conclusão

A arquitetura de microservices, com a decomposição baseada em contextos delimitados e o uso de um API Gateway, pode trazer muitos benefícios em termos de flexibilidade e escalabilidade. No entanto, a decisão de compartilhar uma base de dados deve ser cuidadosamente avaliada, considerando as trade-offs entre simplicidade e acoplamento.