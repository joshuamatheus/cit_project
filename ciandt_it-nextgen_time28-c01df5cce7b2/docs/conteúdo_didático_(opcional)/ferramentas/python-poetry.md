# Python Poetry

Poetry é uma ferramenta de gerenciamento de dependências e construção de pacotes para projetos Python. Ele facilita a criação e a gestão de ambientes virtuais, além de permitir a especificação e a instalação de dependências de maneira simples.

## 1. Instalação do Poetry

### 1.1. Usando o Instalador

Para instalar o Poetry, você pode usar o seguinte comando no terminal:

```bash
curl -sSL https://install.python-poetry.org | python3 -
```

### 1.2. Verificando a Instalação

Após a instalação, verifique se o Poetry foi instalado corretamente com:

```bash
poetry --version
```

## 2. Estrutura do Poetry

### 2.1. Diretório do Projeto

Quando você cria um novo projeto com o Poetry, ele gera uma estrutura de diretórios com um arquivo `pyproject.toml`, que é o principal arquivo de configuração para o seu projeto.

### 2.2. Arquivo pyproject.toml

O arquivo `pyproject.toml` é onde você define as configurações do seu projeto, incluindo metadados e dependências.

```toml
[tool.poetry]
name = "meu_projeto"
version = "0.1.0"
description = "Uma breve descrição do meu projeto."
authors = ["Seu Nome <seu.email@exemplo.com>"]
license = "MIT"
readme = "README.md"

[tool.poetry.dependencies]
python = "^3.8"
requests = "^2.25.1"
flask = "^2.0.1"

[tool.poetry.dev-dependencies]
pytest = "^6.2.4"
black = "^21.6b0"

[build-system]
requires = ["poetry-core>=1.0.0"]
build-backend = "poetry.core.masonry.api"
```

#### Explicação dos Campos

- **[tool.poetry]**: Seção principal onde são configuradas as informações do projeto.
  - `name`: Nome do projeto.
  - `version`: Versão do projeto.
  - `description`: Uma breve descrição do que o projeto faz.
  - `authors`: Lista de autores do projeto.
  - `license`: Licença do projeto.
  - `readme`: Caminho para o arquivo README do projeto.

- **[tool.poetry.dependencies]**: Lista de dependências do projeto.
  - `python`: Especifica a versão do Python necessária.
  - `requests`: Exemplo de uma dependência do projeto.
  - `flask`: Outro exemplo de dependência.

- **[tool.poetry.dev-dependencies]**: Dependências necessárias apenas para desenvolvimento.
  - `pytest`: Ferramenta de testes.
  - `black`: Formatação de código.

- **[build-system]**: Define o sistema de construção do projeto.
  - `requires`: Dependências necessárias para construir o projeto.
  - `build-backend`: Especifica o backend de construção a ser usado.

## 3. Comandos Básicos do Poetry

### 3.1. Criando um Novo Projeto

Para criar um novo projeto, use:

```bash
poetry new nome-do-projeto
```

Isso criará uma estrutura básica de diretório:

```
nome-do-projeto/
├── nome_do_projeto/
│   └── __init__.py
├── tests/
│   └── __init__.py
└── pyproject.toml
```

### 3.2. Adicionando Dependências

Para adicionar uma dependência ao seu projeto, use:

```bash
poetry add nome-do-pacote
```

Por exemplo, para adicionar o pacote `requests`:

```bash
poetry add requests
```

#### 3.2.1. Adicionando Dependências de Desenvolvimento

Para adicionar uma dependência apenas para desenvolvimento, use a flag `--dev`:

```bash
poetry add --dev nome-do-pacote
```

### 3.3. Removendo Dependências

Para remover uma dependência, use:

```bash
poetry remove nome-do-pacote
```

### 3.4. Instalando Dependências

Para instalar todas as dependências listadas no `pyproject.toml`, use:

```bash
poetry install
```

### 3.5. Atualizando Dependências

Para atualizar as dependências do projeto, use:

```bash
poetry update
```

## 4. Gerenciando Ambientes Virtuais

Poetry cria automaticamente um ambiente virtual para o seu projeto. Para ativar o ambiente virtual, use:

```bash
poetry shell
```

Para executar um comando dentro do ambiente virtual sem ativá-lo, você pode usar:

```bash
poetry run nome-do-comando
```

## 5. Scripts de Execução

Você pode definir scripts no seu arquivo `pyproject.toml` para executar comandos personalizados. Por exemplo:

```toml
[tool.poetry.scripts]
meu_script = "modulo:funcao"
```

Para executar o script, use:

```bash
poetry run meu_script
```

## 6. Publicando Pacotes

Se você deseja distribuir seu pacote, pode publicá-lo no PyPI:

1. Primeiro, certifique-se de que as informações no `pyproject.toml` estão corretas.
2. Faça login na sua conta PyPI:

   ```bash
   poetry config pypi-token.pypi <TOKEN>
   ```

3. Publique seu pacote:

   ```bash
   poetry publish
   ```

## 7. Conclusão

O Poetry simplifica o gerenciamento de dependências e a construção de pacotes em projetos Python. Para mais informações e documentação detalhada, você pode visitar a [documentação oficial do Poetry](https://python-poetry.org/docs/).

### 8. Recursos Adicionais

- [Documentação do Poetry](https://python-poetry.org/docs/)
- [Repositório no GitHub](https://github.com/python-poetry/poetry)
- [Guia de Início Rápido](https://python-poetry.org/docs/)

Esses recursos podem ajudá-lo a se aprofundar ainda mais no uso do Poetry e em suas funcionalidades.