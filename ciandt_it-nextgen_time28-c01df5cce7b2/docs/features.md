# Detalhamento das Features

## Cadastro de Colaboradores e PDMs

COMO um administrador de usuários

EU QUERO cadastrar colaboradores e PDMs na plataforma, incluindo nome, email, cargo, se é um PDM e associar diretos

PARA QUE eu possa gerenciar e organizar os usuários da empresa.

### Narrativa do Negócio

Um administrador precisa de uma funcionalidade que permita cadastrar, editar e desativar colaboradores e PDMs, registrando informações como nome, email, cargo e se é um PDM, além de associar esses usuários aos seus diretos.

### Fluxo de Tela

1. O administrador acessa a página de cadastro de usuário.

1. O sistema exibe um formulário de cadastro com campos para nome, email, cargo e um checkbox para indicar se é um PDM.

1. O administrador preenche os campos obrigatórios (nome, email, cargo).

1. O administrador marca o checkbox se o usuário for um PDM.

1. O administrador seleciona os diretos do PDM em uma lista de usuários disponíveis.

1. O administrador clica no botão "Cadastrar".

1. O sistema salva as informações do usuário e registra a data de criação.

1. O sistema exibe uma mensagem de confirmação de cadastro.

1. Para editar um usuário, o administrador acessa a lista de usuários cadastrados e seleciona o usuário desejado.

1. O sistema exibe o formulário preenchido com as informações atuais do usuário.

1. O administrador altera as informações desejadas e clica no botão "Salvar".

1. O sistema salva as alterações e registra a data de edição.

1. Para desativar um usuário, o administrador acessa a lista de usuários cadastrados e seleciona o usuário desejado.

1. O sistema exibe os detalhes do usuário e um botão "Desativar".

1. O administrador clica no botão "Desativar".

1. O sistema desativa o usuário e registra a data de desativação.

### Critérios de Aceitação

1. O sistema deve permitir o cadastro de colaboradores e PDMs.

1. O sistema deve permitir a edição de informações de usuários.

1. O sistema deve permitir a desativação de usuários.

1. O sistema deve registrar as datas de criação, edição e desativação.

1. O sistema deve exibir uma mensagem de confirmação após o cadastro, edição ou desativação de um usuário.

1. O sistema deve tratar possíveis erros e exibir uma mensagem informando o usuário.

## Login na Plataforma para Colaboradores e PDMs

COMO um colaborador ou PDM

EU QUERO fazer login na plataforma

PARA QUE eu possa acessar meus dados e funcionalidades associadas ao meu perfil

### Business Narrative

Permitir que colaboradores e PDMs façam login na plataforma e, caso seja a primeira vez, definir uma senha. Após o login, gerar um token JWT com os dados do usuário nas claims, incluindo as informações do PDM.

### Screen Flow

1. O usuário acessa a página de login.
2. O sistema exibe campos para inserção de e-mail.
3. O usuário insere seu e-mail.
4. O usuário clica no botão "Continuar".
5. Se for a primeira vez que o usuário está fazendo login:
   a. O sistema exibe uma tela para definição de nova senha.
   b. O usuário insere a nova senha e confirma a senha.
   c. O usuário clica no botão "Salvar".
   d. O sistema exibe uma mensagem de confirmação de senha definida com sucesso.
6. O sistema apresenta uma tela para digitar a senha
7. O sistema valida as credenciais:
   a. Se houver erro, exibe mensagem de erro e permite nova tentativa.
   b. Se bem-sucedido, gera um token JWT contendo os dados do usuário nas claims, incluindo os dados do PDM.
8. O sistema redireciona o usuário para a página inicial da plataforma.

### Acceptance Criteria

- O sistema deve permitir que colaboradores e PDMs façam login.
- O sistema deve exibir uma tela para definição de senha se for o primeiro login do usuário.
- O sistema deve gerar um token JWT com os dados do usuário nas claims, incluindo informações do PDM.
- O sistema deve ter um desempenho satisfatório, com tempo de resposta máximo de 2 segundos.
- O admin tbm deve se logar ao sistema, porém o mesmo só é cadastrado através de uma linha de comando

## Criação de Solicitação de Feedback

COMO um colaborador

QUERO criar uma solicitação de feedback

PARA receber feedback personalizado de avaliadores específicos

### Business Narrative

Os colaboradores precisam de uma maneira de solicitar feedback personalizado dos avaliadores escolhidos, garantindo que o processo seja aprovado pelo PDM para manter a qualidade e relevância das avaliações.

### Screen Flow

1. O colaborador acessa a tela de listagem de solicitações de feedback.
2. O colaborador clica no botão "Nova Solicitação".
3. O sistema exibe um formulário para criar a solicitação de feedback.
4. O colaborador preenche no mínimo três perguntas personalizadas abertas.
5. O colaborador adiciona os possíveis avaliadores, inserindo nome e e-mail em uma lista dinâmica.

- Se e-mail do avaliador for CI&T o sistema não deve permitir adicionar, pois apenas avaliadores externos podem fazê-lo

1. O colaborador clica no botão "Salvar Solicitação".
2. O sistema envia um email de notificação ao PDM.
3. O PDM acessa o sistema e visualiza a tela de listagem de feedbacks a aprovar.
4. O PDM aprova ou rejeita a solicitação:
   - Se aprovado, a solicitação não pode ser editada e um email é enviado aos avaliadores.
   - Se rejeitado, o PDM preenche um campo de texto com comentários e o colaborador pode editar as perguntas e enviar novamente para aprovação.
   - Adicionar uma data de aprovação e a partir dessa data a solicitação tem 3 meses de validade para ser respondida

### Acceptance Criteria

- O sistema deve permitir a criação de uma solicitação de feedback com no mínimo três perguntas personalizadas abertas.
- O sistema deve permitir a adição dinâmica de nome e email dos avaliadores.
- O sistema deve enviar um email de notificação ao PDM ao salvar a solicitação.
- O sistema deve permitir que o PDM aprove ou rejeite solicitações de feedback.
- O sistema deve enviar um email aos avaliadores quando uma solicitação for aprovada.
- O sistema deve permitir que o colaborador edite a solicitação se a mesma for rejeitada pelo PDM.
- A interface do sistema deve ser acessível para pessoas com deficiências visuais.
- O sistema deve ter um desempenho satisfatório, com tempo de resposta máximo de 2 segundos.

## Resposta do Feedback solicitado aos avaliadores externos

COMO um colaborador que quer feedback

EU QUERO solicitar respostas sobre minha performance

PARA QUE eu possa melhorar e evoluir no trabalho

### Business Narrative

A funcionalidade deve permitir que colaboradores solicitem feedback anonimamente de clientes externos, garantindo que colaboradores internos não possam dar feedback.

### Screen Flow

1. A pessoa acessa o link fornecido para a pesquisa de feedback.
2. O sistema apresenta uma tela pedindo nome e e-mail do avaliador
   - O sistema verifica se o e-mail está entre os avaliadores escolhidos pelo colaborador, caso não esteja envia para uma tela de não permitido
3. O sistema apresenta um formulário contendo todas as perguntas cadastradas pelo colaborador
4. O usuário clica no botão "Enviar".
5. O sistema exibe uma mensagem de confirmação: "Obrigado pelo seu feedback!"

### Acceptance Criteria

- O sistema deve permitir que apenas clientes externos indicados pelo colaborador forneçam feedback.
- O sistema deve armazenar o feedback associado ao colaborador que solicitou.
- O sistema deve exibir uma mensagem de confirmação após o envio do feedback.
- Antes de ser respondido o questionário o sistema deve validar se o mesmo se encontra dentro do período de validade de 3 meses a partir da data de aprovação do PDM

## Visualização de Feedback

COMO colaborador

QUERO acessar uma tela dedicada no aplicativo para visualizar os feedbacks recebidos

PARA QUE eu possa interpretá-los de forma organizada e fácil.

## Business Narrative

Os colaboradores precisam de uma forma prática e organizada para visualizar os feedbacks recebidos, facilitando a interpretação e o uso dessas informações para seu desenvolvimento.

## Screen Flow

1. O colaborador logado acessa o menu principal do aplicativo.
2. Seleciona a opção "Feedbacks Recebidos".
3. O sistema exibe uma tela com uma lista de feedbacks.
4. O colaborador seleciona um feedback específico para visualizar os detalhes.
5. O sistema exibe o feedback selecionado, com respostas apresentadas de forma anônima.
6. Se o usuário logado for um PDM, ele pode acessar uma seção adicional para visualizar os feedbacks recebidos por seus diretos.
7. O PDM seleciona um feedback de um direto e o sistema exibe as respostas com a identificação de quem respondeu.

## Acceptance Criteria

- O sistema deve permitir que o colaborador visualize feedbacks recebidos.
- Feedbacks para o colaborador logado devem ser exibidos de forma anônima.
- Feedbacks dos diretos de um PDM devem exibir a identificação de quem respondeu.
- O sistema deve exibir uma mensagem de erro em caso de falha na recuperação dos feedbacks.