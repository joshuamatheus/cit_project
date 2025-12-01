# Gestão de Usuários

Esse microserviço é responsável pela gestão de usuários e também pela geração de seu token de acesso (JWT)

## Operações

* **Criar Usuário**: Cria usuário com seu nome, email, cargo, tipo e pdm
* **Editar Usuário**: Edita o dados do usuário
* **Desativar Usuário**: Desativa um usuário com soft delete
* **Checa Senha Cadastrada**: Checa se usuario já tem sua senha cadastrada
* **Obtém Token**: Obtem um token de acesso ao sistema mediante o fornecimento de um email e senha válidos

## Operações em linha de comando

* **Criar usuário administrador**: Criação de um usuario do tipo admin, fornecer nome, email, senha e cargo. O tipo será preenchido automáticamente e o pdm será sempre nulo 

## Dominio identificado

* **Usuário**
* **Usuário Logado**

## Validações

* Apenas usuários do tipo ADMIN podem cadastrar, editar, desativar um usuário
* Usuário admin não tem acesso a senha do colaborador, a mesma será criada pelo próprio colaborador
* O token JWT deve ter a validade de apenas 1 dia

## Pré-definições

* Usar a lib security para validar e gerar os tokens e usuários