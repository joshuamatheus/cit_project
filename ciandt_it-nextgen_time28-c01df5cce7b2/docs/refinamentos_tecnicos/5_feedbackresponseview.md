# Visualização de Respostas de Feedback

Esse microserviço é responsável pela exibição das respostas de feedback tanto para o colaborador de forma anonima, quanto para o PDM de forma não anonima

## Operações

* **Listar Respostas feedback colaborador**: listar as repostas de feedback no contexto de um colaborador (Um PDM pode ser um colaborador também nesse contexto)
* **Visualizar Resposta de Feedback**: Visualizar uma resposta de feedback
* **Solicitar analise de uma IA sobre os feedbacks recebidos**: enviar os feedbacks recebidos para a api do flow analisar

## Domínio identificado

Trata-se inteiramente de um **subdomínio** aonde todas as entidades são **versões do dado** voltado apenas para o **contexto** de visualização

* **Usuário Logado**
* **Pedido de Feedback** 
* **Pergunta**
* **Resposta de Feedback**
* **Resposta de pergunta**
* **Pergunta Respondida**

## Validações

* Colaboradores podem ver somente as respostas recebidas para si mesmos (PDMs também são colaboradores nesse contexto)

* Colaboradores veem as respostas de foma anonima, tanto na listagem quanto na visualização completa

* PDMs veem as respostas com a identificação, porém apenas as de seus diretos, respostas de feedbacks voltadas para ele mesmo continuam anonimas