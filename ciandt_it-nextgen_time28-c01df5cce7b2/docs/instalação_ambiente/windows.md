# Windows

Este guia fornece um passo a passo sobre como instalar o Windows Subsystem for Linux (WSL), Ubuntu e várias ferramentas de desenvolvimento, incluindo OpenJDK 17, Python 3, Node.js, Maven, Jest, MkDocs e Docker CE, utilizando o Chocolatey. 

## Pré-requisitos

1. **Windows 10 ou Superior**: Certifique-se de que você está usando Windows 10 ou uma versão mais recente, pois o WSL está disponível apenas nessas versões.

2. **Permissões de Administrador**: Para instalar o WSL e as ferramentas, você precisará de permissões de administrador.

## Passo 1: Habilitar o WSL

1. **Abrir o PowerShell como Administrador**:
      - Clique com o botão direito do mouse no menu Iniciar e selecione "Windows PowerShell (Admin)".

2. **Habilitar o recurso WSL**:
      - Execute o seguinte comando no PowerShell:

      ```powershell
      wsl --install
      ```

      - Este comando instala a versão padrão do WSL e o Ubuntu.

3. **Reiniciar o Computador**:
      - Após a instalação, você deve reiniciar o computador para que as alterações tenham efeito.

## Passo 2: Instalar o Ubuntu no WSL

Após reiniciar, o Ubuntu deve ser instalado automaticamente. Caso contrário, siga os passos abaixo:

1. **Abrir a Microsoft Store**:
      - No menu Iniciar, busque por "Microsoft Store" e abra-a.

2. **Buscar por Ubuntu**:
      - Na loja, use a barra de pesquisa para procurar "Ubuntu". Você verá várias versões disponíveis (como Ubuntu 20.04 LTS, Ubuntu 22.04 LTS, etc.).

3. **Instalar o Ubuntu**:
      - Selecione a versão desejada e clique em "Instalar".

4. **Iniciar o Ubuntu**:
      - Após a instalação, você pode iniciar o Ubuntu diretamente do menu Iniciar. Na primeira execução, você será solicitado a criar um nome de usuário e uma senha.

## Passo 3: Atualizar o Sistema Ubuntu

1. **Abrir o Terminal do Ubuntu**:
      - Após a instalação, abra o Ubuntu a partir do menu Iniciar.

2. **Atualizar a lista de pacotes**:
      - Execute os seguintes comandos para atualizar a lista de pacotes e instalar as atualizações disponíveis:

      ```bash
      sudo apt update
      sudo apt upgrade -y
      ```

## Passo 4: Instalar o Chocolatey

1. **Abrir o PowerShell como Administrador**:
      - Se ainda não estiver aberto, inicie o PowerShell como administrador novamente.

2. **Verificar se o Chocolatey já está instalado**:
      - Execute o seguinte comando para verificar se o Chocolatey já está instalado:

      ```powershell
      WHERE choco
      ```

      - Se o comando retornar um erro, isso significa que o Chocolatey não está instalado.

3. **Instalar o Chocolatey**:
      - Execute o seguinte comando no PowerShell para instalar o Chocolatey:

      ```powershell
      Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
      ```

4. **Adicionar Chocolatey ao PATH**:
      - Após a instalação, adicione o Chocolatey ao PATH. O comando acima já faz isso, mas você pode verificar se a variável `PATH` foi atualizada corretamente.

## Passo 5: Instalar Ferramentas de Desenvolvimento com Chocolatey

Agora que o Chocolatey está instalado, você pode usar o seguinte processo para instalar as ferramentas necessárias.

### 5.1: Atualizar o Chocolatey

Execute o seguinte comando para garantir que você esteja usando a versão mais recente do Chocolatey:

```powershell
choco upgrade chocolatey -y
```

### 5.2: Instalar OpenJDK 17

- **Propósito**: OpenJDK é a implementação de código aberto do Java, necessário para desenvolver e executar aplicações Java.

Execute o seguinte comando:

```powershell
choco install openjdk17 -y
```

### 5.3: Instalar Python 3

- **Propósito**: Python é uma linguagem de programação amplamente utilizada para desenvolvimento web, automação, análise de dados, etc.

Execute o seguinte comando:

```powershell
choco install python --version=3.12.0 -y
```

### 5.4: Instalar Node.js

- **Propósito**: Node.js permite a execução de código JavaScript no lado do servidor, ideal para desenvolvimento de aplicações web.

Execute o seguinte comando:

```powershell
choco install nodejs -y
```

### 5.5: Instalar Maven

- **Propósito**: Maven é uma ferramenta de gerenciamento de projetos e automação de builds para projetos Java.

Execute o seguinte comando:

```powershell
choco install maven -y
```

### 5.6: Instalar Jest

- **Propósito**: Jest é um framework de testes para JavaScript.

Execute o seguinte comando:

```powershell
npm install -g jest
```

### 5.7: Instalar MkDocs e Poetry

Para instalar MkDocs e Poetry, que são ferramentas úteis para documentação e gerenciamento de pacotes Python, execute os comandos abaixo:

- **MkDocs**: MkDocs é uma ferramenta para criar documentação estática de projetos.

```bash
pip install mkdocs
```

- **Poetry**: Poetry é uma ferramenta para gerenciar dependências e empacotar projetos Python.

```bash
pip install poetry
```

### 5.8: Instalar Docker CE

- **Propósito**: Docker é uma plataforma para desenvolver, enviar e executar aplicações em contêineres.

Execute os comandos abaixo no terminal do Ubuntu WSL:

```bash
sudo apt install -y apt-transport-https ca-certificates curl
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
echo "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list
sudo apt update
sudo apt install -y docker-ce
```

## Passo 6: Verificar as Instalações

Após instalar todas as ferramentas, você pode verificar se tudo foi instalado corretamente. Execute os seguintes comandos no terminal do Windows:

```powershell
java -version
python --version
node --version
mvn --version
jest --version
mkdocs --version
poetry --version
wsl -d Ubuntu -- docker --version
```

## Passo 7: Instalar o git for windows

Acessar a página https://git-scm.com/downloads/win para baixar e instalar o client git oficial para windows

## Descrição dos Pacotes Instalados

- **Git**: Ferramenta de versionamento de código usada para centralizar as alterações de código em um repositorio remoto
- **OpenJDK 17**: Ambiente de desenvolvimento Java.
- **Python 3**: Linguagem de programação versátil.
- **Node.js**: Ambiente de execução para JavaScript.
- **Maven**: Gerenciador de projetos Java.
- **Jest**: Framework de testes para JavaScript.
- **MkDocs**: Ferramenta para documentação estática.
- **Poetry**: Gerenciador de dependências para Python.
- **Docker CE**: Plataforma para contêineres de aplicativos.

## Conclusão

Com este guia, você instalou o Ubuntu no WSL e configurou um ambiente de desenvolvimento completo no Windows. Se você encontrar qualquer problema durante a instalação, consulte a documentação oficial de cada ferramenta ou busque ajuda em comunidades online.

## Automação

Alternativamente, pode usar o script windows.bat contido na pasta install_scripts, esse script assume que já performou a instalação do Ubuntu wsl e que está rodando como administrador