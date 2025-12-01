#!/bin/bash

# instala o Homebrew
curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh

# Atualiza o Homebrew
brew update

# Instala OpenJDK 17, Maven, Python3, Pip, Pipx, Node.js e npm
brew install openjdk@17 maven python3 node

# Instala o Podman
brew install podman

# Instala o Podman Compose
brew install podman-compose

# Inicializa e inicia a máquina Podman
podman machine init
podman machine start

# Instala Jest globalmente
npm install -g jest

# Instala o mkdocs globalmente usando pipx
pipx install mkdocs --include-deps --force
pipx inject mkdocs mkdocs-material mkdocs-simple-plugin plantuml-markdown mkdocs-mermaid2-plugin mkdocs_puml --include-deps --force
pipx ensurepath

# Carrega as configurações do shell
source ~/.bash_profile  # Use ~/.zshrc se estiver usando zsh

# Exibe as versões instaladas para verificação
echo "OpenJDK version:"
java -version

echo "Python version:"
python3 --version

echo "MkDocs version:"
mkdocs --version

echo "Node.js version:"
node --version

echo "Podman version:"
podman --version

echo "Maven version:"
mvn --version

echo "Jest version:"
jest --version

echo "Poetry version:"

# Instalar Poetry se necessário, descomente a linha abaixo
curl -sSL https://install.python-poetry.org | python3 -

# Para verificar a versão do Poetry se já instalado
poetry --version

echo "Instalação concluída!"