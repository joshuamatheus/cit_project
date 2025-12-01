# Mac OS

Este guia irá orientá-lo na instalação manual das ferramentas OpenJDK, Maven, Python3, Node.js, Podman, Jest e MkDocs, além de outros pacotes. Siga os passos abaixo cuidadosamente.

## Passo 1: Instalar o Homebrew

Homebrew é um gerenciador de pacotes para macOS e Linux. Para instalá-lo, siga os passos abaixo:

* **Abra o Terminal.**

* **Cole o seguinte comando e pressione Enter:**

      ```bash
      /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
      ```

* **Siga as instruções na tela.** Você pode ser solicitado a inserir sua senha de administrador.

* **Após a instalação, adicione o Homebrew ao seu PATH.** Para isso, adicione a seguinte linha ao seu `~/.bash_profile` (bash) ou `~/.zshrc` (zsh):

      ```bash
      export PATH="/opt/homebrew/bin:$PATH"
      ```

* **Atualize seu terminal para aplicar as alterações:**

      ```bash
      source ~/.bash_profile  # Use ~/.zshrc se estiver usando zsh
      ```

## Passo 2: Instalar OpenJDK 17

* **No terminal, execute o seguinte comando:**

      ```bash
      brew install openjdk@17
      ```

* **Após a instalação, configure as variáveis de ambiente (se necessário):**

      Adicione ao seu `~/.bash_profile` ou `~/.zshrc`:

      ```bash
      export JAVA_HOME="$(brew --prefix openjdk@17)"
      export PATH="$JAVA_HOME/bin:$PATH"
      ```

* **Atualize seu terminal:**

      ```bash
      source ~/.bash_profile  # Use ~/.zshrc se estiver usando zsh
      ```

* **Verifique a instalação:**

      ```bash
      java -version
      ```

## Passo 3: Instalar Maven

* **No terminal, execute:**

      ```bash
      brew install maven
      ```

* **Verifique a instalação:**

      ```bash
      mvn --version
      ```

## Passo 4: Instalar Python3 e Pip

* **Instale Python3:**

      ```bash
      brew install python3
      ```

* **Verifique a instalação:**

      ```bash
      python3 --version
      ```

* **Pip é instalado automaticamente com o Python3. Para verificar o Pip:**

      ```bash
      pip3 --version
      ```

## Passo 5: Instalar Node.js e npm

* **Instale Node.js:**

      ```bash
      brew install node
      ```

* **Verifique a instalação do Node.js e npm:**

      ```bash
      node --version
      npm --version
      ```

## Passo 6: Instalar Podman

* **Instale o Podman:**

      ```bash
      brew install podman
      ```

* **Verifique a instalação:**

      ```bash
      podman --version
      ```

* **Inicialize e inicie a máquina Podman:**

      ```bash
      podman machine init
      podman machine start
      ```

## Passo 7: Instalar Jest

* **Instale Jest globalmente usando npm:**

      ```bash
      npm install -g jest
      ```

* **Verifique a instalação:**

      ```bash
      jest --version
      ```

## Passo 8: Instalar MkDocs e Pipx

* **Instale o Pipx:**

      ```bash
      brew install pipx
      ```

      Após isso, adicione o diretório do Pipx ao seu PATH (adicionar ao `~/.bash_profile` ou `~/.zshrc`):

      ```bash
      export PATH="$HOME/.local/bin:$PATH"
      ```

* **Atualize seu terminal:**

      ```bash
      source ~/.bash_profile  # Use ~/.zshrc se estiver usando zsh
      ```

* **Instale MkDocs usando Pipx:**

      ```bash
      pipx install mkdocs --include-deps
      ```

* **Instale plugins do MkDocs (opcional):**

      ```bash
      pipx inject mkdocs mkdocs-material mkdocs-simple-plugin plantuml-markdown mkdocs-mermaid2-plugin mkdocs_puml
      ```

* **Verifique a instalação do MkDocs:**

      ```bash
      mkdocs --version
      ```

## Passo 9: Instalar Poetry

* **Instale o Poetry:**

      ```bash
      pipx install poetry
      ```

* **Verifique a instalação do Poetry:**

      ```bash
      poetry --version
      ```