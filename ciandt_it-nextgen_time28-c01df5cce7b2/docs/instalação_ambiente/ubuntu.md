# Ubuntu

Este documento fornece um passo a passo sobre como instalar várias ferramentas de desenvolvimento, incluindo OpenJDK 17, Python 3, Docker, Node.js, Maven, Jest e MkDocs em um sistema Ubuntu. Ao final, será apresentada uma descrição de cada pacote instalado.

## Passo a Passo

### 1. Atualizar a Lista de Pacotes

```bash
sudo apt update
```

**Descrição:** Este comando atualiza a lista de pacotes disponíveis e suas versões no repositório. É importante executar este passo antes de instalar qualquer pacote, pois garante que você esteja instalando as versões mais recentes e estáveis dos softwares.

### 2. Instalar OpenJDK 17, Python 3, Maven, e Ferramentas Adicionais

```bash
sudo apt install -y git openjdk-17-jdk maven python3 python3-pip python3-poetry pipx nodejs npm apt-transport-https ca-certificates curl software-properties-common
```

**Descrição:**

- **openjdk-17-jdk**: É a implementação de código aberto da plataforma Java. O JDK (Java Development Kit) é necessário para desenvolver e executar aplicações Java.

- **maven**: É uma ferramenta de gerenciamento de projetos e automação de builds para projetos Java. Facilita a construção, gerenciamento de dependências e documentação do projeto.

- **python3**: É a linguagem de programação Python, amplamente utilizada para desenvolvimento web, automação, análise de dados e muito mais.

- **python3-pip**: É o gerenciador de pacotes para Python, permitindo que você instale e gerencie bibliotecas e dependências de Python.

- **python3-poetry**: É uma ferramenta para gerenciar dependências e empacotar projetos Python. Facilita a criação e manutenção de ambientes de desenvolvimento.

- **pipx**: É uma ferramenta para instalar e executar aplicativos Python em ambientes isolados. Permite que você instale pacotes Python globalmente sem conflitos.

- **nodejs**: É um ambiente de execução JavaScript que permite a execução de código JavaScript no lado do servidor.

- **npm**: É o gerenciador de pacotes para Node.js, usado para instalar bibliotecas e pacotes JavaScript.

- **apt-transport-https**: Permite que o APT use HTTPS para acessar repositórios de pacotes, melhorando a segurança.

- **ca-certificates**: É um pacote que contém certificados de autoridades de certificação, garantindo conexões seguras.

- **curl**: É uma ferramenta de linha de comando para transferir dados usando URLs, frequentemente utilizada para baixar arquivos e acessar APIs.

- **software-properties-common**: Fornece um conjunto de ferramentas para gerenciar repositórios de software.

### 3. Adicionar o Repositório do Docker

```bash
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

**Descrição:** Esses comandos adicionam a chave GPG do Docker e o repositório oficial do Docker ao sistema. Isso é necessário para garantir que você esteja instalando o Docker a partir de uma fonte confiável e que as atualizações sejam corretamente gerenciadas.

### 4. Atualizar a Lista de Pacotes Novamente

```bash
sudo apt update
```

**Descrição:** Após adicionar o repositório do Docker, é necessário atualizar a lista de pacotes novamente para incluir os pacotes disponíveis a partir do novo repositório.

### 5. Instalar o Docker

```bash
sudo apt install -y docker-ce
```

**Descrição:** Este comando instala o Docker Community Edition (CE), que permite criar, implantar e gerenciar contêineres de aplicativos.

### 6. Instalar Jest Globalmente

```bash
npm install -g jest
```

**Descrição:** Jest é um framework de testes para JavaScript. Este comando instala Jest globalmente, permitindo que você o use em qualquer projeto no seu sistema.

### 7. Instalar MkDocs Globalmente

```bash
pipx install mkdocs --include-deps
pipx inject mkdocs mkdocs-material mkdocs-simple-plugin plantuml-markdown mkdocs-mermaid2-plugin mkdocs_puml --include-deps --force
pipx ensurepath
source ~/.bashrc
```

**Descrição:**

- **mkdocs**: É uma ferramenta para criar documentação estática de projetos. Este comando instala MkDocs em um ambiente isolado, garantindo que suas dependências não conflitem com outros projetos.

- **pipx ensurepath**: Adiciona o diretório de scripts do pipx ao seu PATH, permitindo que você execute as ferramentas instaladas facilmente.

- **source ~/.bashrc**: Recarrega o arquivo `.bashrc` para aplicar as mudanças feitas no PATH.

### 8. Verificar as Versões Instaladas

Após a instalação, você pode verificar as versões instaladas de cada ferramenta com os seguintes comandos:

```bash
echo "OpenJDK version:"
java -version

echo "Python version:"
python3 --version

echo "MkDocs version"
mkdocs --version

echo "Node.js version:"
node --version

echo "Docker version:"
docker --version

echo "Maven version:"
mvn --version

echo "Jest version:"
jest --version

echo "Poetry version:"
poetry --version

echo "Instalação concluída!"
```

**Descrição:** Esses comandos exibem as versões instaladas de cada ferramenta, permitindo que você verifique se a instalação foi bem-sucedida.

## Descrição dos Pacotes Instalados

- **Git**: Ferramenta de versionamento de código usada para centralizar as alterações de código em um repositorio remoto
- **OpenJDK 17**: Ambiente de desenvolvimento Java.
- **Maven**: Gerenciador de projetos e automação de builds para Java.
- **Python 3**: Linguagem de programação.
- **pip**: Gerenciador de pacotes para Python.
- **Poetry**: Gerenciador de dependências e empacotador para Python.
- **pipx**: Ferramenta para instalar aplicativos Python em ambientes isolados.
- **Node.js**: Ambiente de execução para JavaScript.
- **npm**: Gerenciador de pacotes para Node.js.
- **Docker**: Plataforma para desenvolver, enviar e executar aplicações em contêineres.
- **Jest**: Framework de testes para JavaScript.
- **MkDocs**: Ferramenta para criar documentação estática.

Com este guia, você deve ser capaz de instalar todas as ferramentas necessárias para o seu ambiente de desenvolvimento no Ubuntu. Se você tiver alguma dúvida ou encontrar problemas durante a instalação, sinta-se à vontade para pedir ajuda!

## Automação
Caso queira, basta executar o script ubuntu.sh contido na pasta install_scripts do projeto, contem todos esses passos automatizados