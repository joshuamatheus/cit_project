# Pedido de Feedback

Esse microserviço é responável por armazenar os pedidos de feedback e gerenciar o estado do mesmo.

## Operações

* **Criar Pedido de Feedback**: Recebe o pedido juntamente com as perguntas dinamicas e dados de seu criador
* **Listar Pedidos de Feedback**: Lista os pedidos que pertencem ao colaborador logado
* **Listar Pedidos de Feedback a aprovar**: Lista os pedidos de feedback que um PDM tem pendente de aprovação
* **Aprovar Pedido de Feedback**: PDM aprovar requisição de feedback e enviar para analisadores
* **Editar Feedback**: Colaborador editar feedbacks não aprovados

## Operações em background

* **Envio de email de notificação para PDMS**: Novo feedback a aprovar
* **Envio de email de notificação para Avaliadores**: Novo feedback a responder 
* **Envio de email de notificação para Colaboradores**: Feedback Aprovado

## Dominio identificado

* **Colaborador**
* **PDM**
* **Pedido de Feedback**
* **Pergunta**
* **Avaliador**

## Validações

* Pedido de feedback editável apenas quando em estado de "rejeitado"
* Avaliadores precisam ter emails externos a CI&T
* Apenas PDMs podem aprovar requisições de feedback
* Colaboradores só podem ver suas próprias requisições na listagem
* PDMs também são colaboradores e podem criar pedidos de feedback
* PDMs não podem aprovar seus próprios pedidos de feedback, apenas seu PDM (PDM do PDM enquanto colaborador)
* PDMs só podem ver requisições a aprovar de seus diretos