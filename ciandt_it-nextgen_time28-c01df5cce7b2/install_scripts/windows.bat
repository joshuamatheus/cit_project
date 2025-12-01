@echo off
SETLOCAL

Set-ExecutionPolicy AllSigned

:: Verifica se o Chocolatey já está instalado
WHERE choco >nul 2>nul
IF %ERRORLEVEL% NEQ 0 (
    :: Instala o Chocolatey
    echo Installing Chocolatey...
    @"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
)

:: Atualiza o Chocolatey
echo Updating Chocolatey...
choco upgrade chocolatey -y

:: Instala as ferramentas necessárias
echo Installing OpenJDK 17...
choco install openjdk17 -y

echo Installing Python 3...
choco install python --version=3.12.0 -y

echo Installing Node.js...
choco install nodejs -y

echo Installing Maven...
choco install maven -y

echo Installing Jest...
call npm install -g jest

echo Installing python poetry and mkdocs
pip install poetry mkdocs mkdocs-material mkdocs-simple-plugin plantuml-markdown mkdocs-mermaid2-plugin mkdocs_puml

echo Installing Docker CE in WSL Ubuntu...
:: Executa os comandos para instalar o Docker no Ubuntu WSL
wsl -d Ubuntu -- bash -c "sudo apt update && sudo apt install -y apt-transport-https ca-certificates curl && curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - && echo \"deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable\" | sudo tee /etc/apt/sources.list.d/docker.list && sudo apt update && sudo apt install -y docker-ce"

:: Exibe as versões instaladas para verificação
echo Checking installed versions...
echo OpenJDK version:
java -version

echo Python version:
python --version

echo Node.js version:
node --version

echo Maven version:
call mvn --version

echo Jest version:
jest --version

echo MkDocs version:
mkdocs --version

echo Poetry version:
poetry --version

echo Installation completed successfully!

ENDLOCAL
pause