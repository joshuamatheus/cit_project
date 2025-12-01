# Respostas de Feedback

Esse microserviço é responsável pelo registro das respostas de feedback feita pelos avaliadores

## Operações

* **Validar e conceder acesso ao avaliador**: Como o avaliador não é um usuario do sistema, precisa apenas validar o email do mesmo
* **Registrar Respostas de feedback**: registrar as respostas de um avaliador a determinada requisição

## Operações em Background

* **Enviar email de notificação**: enviar email para para PDM e Colaborador quando um feedback é respondido

## Domínio identificado

Nesse contexto, não precisamos das entidades de solicitação de feedback, tampouco da entidade de perguntas, basta salvarmos seus ids nos campos de relacionamento para que possamos em outro contexto fazer a junção do dado

* **Usuário Logado**
* **Resposta de Feedback** 
* **Resposta de pergunta**

## Validações

* Antes de responder a solicitação, validar a validade da mesma, cuja é apenas 3 meses após a aprovação do PDM

* Receber o email do avaliador e manter na sessão (através de um token?) e verificar se o avaliador identificado está entre os avaliadores escolhidos pelo colaborador 