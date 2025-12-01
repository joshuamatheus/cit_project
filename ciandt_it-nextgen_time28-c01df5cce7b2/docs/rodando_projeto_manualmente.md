# Rodando o projeto manualmente

## Pré-requisitos

Antes de iniciar, você precisa ter as seguintes ferramentas instaladas em sua máquina:

- **Java JDK (versão 17 ou superior)**
- **Maven**
- **Node.js (versão 18 ou superior)**
- **Python (versão 3.12 ou superior)**
- **PostgreSQL**

> **Nota:** Siga o guia de instalação para sua máquina incluso nessa documentação: [Windows](instalação_ambiente/windows.md), [Ubuntu](instalação_ambiente/ubuntu.md) ou [MacOS](instalação_ambiente/macos.md)

## Instalando o PostgreSQL

### Windows

#### Via Chocolatey
1. Abra o terminal como Administrador.
2. Execute o seguinte comando:
   ```bash
   choco install postgresql
   ```

#### Via MSI
1. Acesse [a página de downloads do PostgreSQL](https://www.postgresql.org/download/windows/).
2. Baixe o instalador MSI.
3. Siga as instruções do assistente para concluir a instalação.

### Linux

#### Via APT (Debian/Ubuntu)
1. Abra o terminal.
2. Atualize o índice de pacotes:
   ```bash
   sudo apt update
   ```
3. Instale o PostgreSQL:
   ```bash
   sudo apt install postgresql postgresql-contrib
   ```

### macOS

#### Via Homebrew
1. Abra o terminal.
2. Instale o PostgreSQL usando Homebrew:
   ```bash
   brew install postgresql
   ```
3. Inicie o serviço do PostgreSQL:
   ```bash
   brew services start postgresql
   ```

### Docker
1. Abra o terminal bash (wsl no Windows)
2. Rode o comando que executa o postgres
   ```bash
   docker run -d --name pgfeedback -e POSTGRES_PASSWORD=root -e POSTGRES_DB=feedback -p 5432:5432 postgres 
   ```
3. O comando serve apenas para a primeira vez que está rodando o container, nas vezes subsequentes usar o comando
   ```bash
   docker start pgfeedback
   ```

> <span style="color:red">No caso da instalação do postgre fora do ambiente docker, será necessário usar uma ferramenta de gerenciamento de banco como o [dbeaver](https://dbeaver.io/) para configurar a base inicial do sistema **feedback**<span>

## Rodando o Projeto Manualmente

### 1. Configurar o Banco de Dados

Certifique-se de que o PostgreSQL esteja em execução. Você pode iniciar o serviço conforme as instruções acima, dependendo do seu sistema operacional.

### 2. Configurar Variáveis de Ambiente

Defina as seguintes variáveis de ambiente:

* **Linux e Mac OS**
      
      ```bash
      export DB_HOST=localhost
      export DB_PORT=5432
      export DB_USER=postgres
      export DB_PASS=root
      export JWT_SECRET_KEY=4Z^XrroxR@dWxqf$!@#qGr4P
      export JWT_ISSUER=user-api
      export DATABASE_URL=postgresql://postgres:root@localhost:5432/feedback
      ```

* **Windows**
      
      ```powershell
      $env:DB_HOST="localhost"
      $env:DB_PORT="5432"
      $env:DB_USER="postgres"
      $env:DB_PASS="root"
      $env:JWT_SECRET_KEY="4Z^XrroxR@dWxqf$!@#qGr4P"
      $env:JWT_ISSUER="user-api"
      $env:DATABASE_URL="postgresql://postgres:root@localhost:5432/feedback"
      ```


### 3. Rodar os Microserviços Java

Acesse o diretório de cada microserviço, construa e inicie os serviços:

Entrar na pasta dos projetos JAVA

```bash
cd java_backend
```

Buildar os projetos

```bash
mvn clean install -DskipTests
```
Rodar usermanagement

```bash
mvn -pl microservices/usermanagement spring-boot:run
```

Rodar feedbackrequest

```bash
mvn -pl microservices/feedbackrequest spring-boot:run
```

Rodar feedbackresponse

```bash
mvn -pl microservices/feedbackresponse spring-boot:run
```

### 4. Rodar o Microserviço Python

Entrar na pasta do projeto

```bash
# Rodar feedbackresponseview
cd python_backend/feedbackresponseview
```

Instale as dependencias

```bash
poetry install
```

Rodando o projeto

```bash
poetry run uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

### 5. Rodar o API Gateway (Node.js)

```bash
cd node_backend/api_gateway
npm install
npm run start
```

### 6. Rodar o Nginx

#### Ubuntu

1. **Pré-requisitos**  
   Certifique-se de que o Nginx esteja instalado no Ubuntu. Caso não esteja, instale-o com:  
   ```bash
   sudo apt update
   sudo apt install nginx
   ```

2. **Copiar o Arquivo de Configuração**  
   Supondo que o arquivo de configuração local esteja em uma pasta `gateway` e se chame `nginx.conf`, copie-o para o diretório de configuração do Nginx:
   ```bash
   sudo cp gateway/nginx.conf /etc/nginx/sites-enabled/default
   ```

3. **Alterar a Configuração com sed**  
   Se for necessário substituir valores (por exemplo, alterar o host para `localhost` em alguma linha de configuração), use:
   ```bash
   sudo sed -Ei 's/(server\s+)[^:]+(:[0-9]+;)/\1localhost\2/g' /etc/nginx/sites-enabled/default
   ```
   *Explicação do comando:*  
   - `sed -Ei` ativa o modo de edição in-place com expressão regular estendida.  
   - O comando procura uma linha que contenha `server` seguido de algum espaço e em seguida qualquer valor até encontrar um número de porta (por exemplo, `:80;`) e substitui esse valor por `localhost` mantendo a porta.

4. **Reiniciar o Nginx**  
   Após as alterações, reinicie o serviço para que as mudanças entrem em vigor:
   ```bash
   sudo service nginx restart
   ```

#### macOS

No macOS, há algumas considerações, principalmente no fato de que o sed pode ter diferenças (o macOS usa o BSD sed). Além disso, o Nginx pode ser instalado via Homebrew.

1. **Instalar o Nginx**  
   Utilize o Homebrew para instalar o Nginx (caso ainda não esteja instalado):
   ```bash
   brew install nginx
   ```
   O arquivo de configuração padrão do Nginx no macOS (via Homebrew) geralmente é encontrado em `/usr/local/etc/nginx/nginx.conf`. Se você estiver usando um arquivo de sites, pode ser necessário adaptar o caminho, por exemplo:
   ```bash
   /usr/local/etc/nginx/servers/default.conf
   ```

2. **Copiar o Arquivo de Configuração**  
   Supondo que você deseja substituir o arquivo de configuração de um site:
   ```bash
   sudo cp gateway/nginx.conf /usr/local/etc/nginx/servers/default.conf
   ```

3. **Alterar a Configuração com sed**  
   Devido às diferenças do BSD sed, a sintaxe pode precisar da flag `-E` e uso de uma extensão de backup (por exemplo, `.bak`). Um exemplo:
   ```bash
   sudo sed -i.bak -E 's/(server\s+)[^:]+(:[0-9]+;)/\1localhost\2/g' /usr/local/etc/nginx/servers/default.conf
   ```
   Esse comando cria um backup do arquivo original com a extensão `.bak`. Se preferir, você pode remover esse backup posteriormente.

4. **Reiniciar o Nginx**  
   Se o Nginx foi instalado via Homebrew, reinicie-o com:
   ```bash
   brew services restart nginx
   ```

#### Windows

No Windows, há duas abordagens que você pode adotar para executar essas tarefas:

**Usando o Chocolatey**

O [Chocolatey](https://chocolatey.org/) é um gerenciador de pacotes para Windows. Você pode utilizá-lo para instalar o Nginx e executar scripts no PowerShell.  
  
**Passos:**

1. **Instalar o Nginx via Chocolatey**  
   Abra o PowerShell com privilégios de administrador e execute:
   ```powershell
   choco install nginx -y
   ```
   Geralmente, o Nginx instalado via Chocolatey ficará em:  
   `C:\tools\nginx\conf\nginx.conf` ou em um caminho similar. Verifique a documentação do pacote.

2. **Copiar o Arquivo de Configuração**  
   Se o seu arquivo local estiver, por exemplo, em `C:\projetos\gateway\nginx.conf`, execute:
   ```powershell
   Copy-Item -Path "C:\projetos\gateway\nginx.conf" -Destination "C:\tools\nginx\conf\nginx.conf" -Force
   ```

3. **Alterar a Configuração**  
   Para alterar o arquivo usando uma ferramenta similar ao sed no Windows, você pode usar o PowerShell com expressões regulares. Por exemplo:
   ```powershell
   (Get-Content "C:\tools\nginx\conf\nginx.conf") -replace '(server\s+)[^:]+(:[0-9]+;)', '$1localhost$2' |
     Set-Content "C:\tools\nginx\conf\nginx.conf"
   ```

4. **Reiniciar o Nginx**  
   Depende de como o Nginx foi configurado para ser executado. Se ele estiver rodando como um serviço, reinicie-o. Se não, feche e inicie novamente usando o atalho ou script do próprio Nginx, por exemplo:
   ```powershell
   # Supondo que você tenha um script para parar e iniciar:
   Stop-Process -Name nginx -Force
   Start-Process "C:\tools\nginx\nginx.exe"
   ```

**WSL (Windows Subsystem for Linux)**

Siga as instruções para o ubuntu


### 7. Acessar a Aplicação

A depender das portas aonde configurou os serviços, terá de acessar pelas portas escolhidas, cuidado ao rodar multiplas aplicações spring, elas podem cabar subindo na mesma porta, alterar o application property para mudar a porta desejada de cada uma