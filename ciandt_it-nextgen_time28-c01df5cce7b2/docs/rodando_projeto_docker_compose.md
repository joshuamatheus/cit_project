# Rodando o projeto com Docker

Este documento irá guiá-lo sobre como utilizar o Docker Compose com a configuração fornecida.

## Executando o Docker Compose

Para iniciar os serviços definidos no seu arquivo `docker-compose.yml`, você pode usar o seguinte comando no seu terminal:

```bash
docker compose up
```

### Efeito da opção `--build`

Se você precisar garantir que as imagens dos seus serviços sejam reconstruídas antes de serem iniciadas, você pode usar:

```bash
docker compose up --build
```

A opção `--build` força a reconstrução das imagens. Isso é especialmente útil quando você fez alterações no código-fonte ou nos arquivos de configuração que impactam a construção da imagem. Assim, você garante que as últimas alterações sejam aplicadas antes que os containers sejam iniciados.

### Efeito da opção `-d`

Adicionando a opção `-d`, o comando se torna:

```bash
docker compose up -d
```

A opção `-d` (detached mode) faz com que os serviços sejam executados em segundo plano. Isso significa que você poderá continuar utilizando o terminal para outras tarefas enquanto os containers estão rodando em background. Sem essa opção, você verá os logs dos serviços sendo executados no terminal, e você precisará parar o processo (Ctrl+C) para interromper a execução.

## Exemplo de Output do Comando `docker compose ls`

Se você executar o comando:

```bash
docker compose ls
```

Um detalhamento sobre os serviços listados no yaml serão exibidos:

| NAME                                         | IMAGE                                    | SERVICE                | STATUS         | PORTS                     |
|----------------------------------------------|------------------------------------------|------------------------|----------------|---------------------------|
| nextgen_base_java-db-1                      | postgres:latest                          | db                     | Up 6 seconds   | 5432/tcp                  |
| nextgen_base_java-feedbackrequest-1         | nextgen_base_java-feedbackrequest        | feedbackrequest        | Up 5 seconds   | 8080/tcp                  |
| nextgen_base_java-feedbackresponse-1        | nextgen_base_java-feedbackresponse       | feedbackresponse       | Up 5 seconds   | 8080/tcp                  |
| nextgen_base_java-feedbackresponseview-1    | nextgen_base_java-feedbackresponseview   | feedbackresponseview   | Up 5 seconds   | 8000/tcp                  |
| nextgen_base_java-nginx-1                   | nginx:alpine                             | nginx                  | Up 4 seconds   | 0.0.0.0:80->80/tcp        |
| nextgen_base_java-usermanagement-1          | nextgen_base_java-usermanagement         | usermanagement         | Up 5 seconds   | 8080/tcp                  |


## Parando os Serviços

Para parar todos os serviços em execução, você pode usar:

```bash
docker compose stop
```

Esse comando interrompe os containers, mas não os remove. Eles poderão ser reiniciados mais tarde.

## Removendo os Serviços

Se você deseja parar e remover todos os containers, redes e volumes associados, você deve usar:

```bash
docker compose down
```

Esse comando garante que tudo relacionado ao projeto seja removido, liberando os recursos utilizados.

## Visualizando os Logs dos Serviços

Para visualizar os logs de todos os serviços, você pode usar:

```bash
docker compose logs
```

### Visualizando os Logs de um Único Serviço

Se você deseja ver os logs de um único serviço, por exemplo, `usermanagement`, utilize:

```bash
docker compose logs usermanagement
```

### Seguindo os Logs

Para seguir os logs em tempo real, você pode usar a opção `-f`:

```bash
docker compose logs -f
```

Ou, para um único serviço:

```bash
docker compose logs -f usermanagement
```

A opção `-f` (follow) mantém a conexão aberta e exibe novas entradas de log à medida que elas são geradas.

## Acessando a aplicação
Agora você pode acessar os serviços em execução através do Nginx que está escutando na porta 80. (http://localhost)