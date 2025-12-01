# Api Gateway com Proxy Reverso e Upstream no NGINX

Este documento descreve a configuração de um servidor reverso utilizando Nginx, projetado para gerenciar três serviços diferentes: `usermanagement`, `feedbackrequest` e `feedbackresponse`. Cada serviço está associado a uma URL específica no servidor principal, que escuta na porta 80.

# Configuração do Servidor

O código de configuração define três blocos `upstream` que especificam os servidores backend para cada serviço:

- `usermanagement`: servidor de gerenciamento de usuários.
- `feedbackrequest`: servidor para requisições de feedback.
- `feedbackresponse`: servidor para respostas de feedback.

Cada serviço está configurado para rodar na porta `8080`.

## Bloco de Servidor

O servidor principal escuta na porta `80` e tem três localizações definidas, cada uma encaminhando requisições para o respectivo serviço backend usando `proxy_pass`.

### Localizações:

- `/users`: Redireciona para o serviço `usermanagement`.
- `/requests`: Redireciona para o serviço `feedbackrequest`.
- `/responses`: Redireciona para o serviço `feedbackresponse`.

Os trechos de código comentados usando `rewrite` são exemplos de como reescrever URLs, mas estão desativados na configuração atual.

# Exemplos de Uso

Para acessar cada serviço, você pode fazer requisições HTTP para as seguintes URLs:

1. Para o serviço de gerenciamento de usuários:
   ```
   GET http://<servidor>/users
   ```

2. Para o serviço de requisições de feedback:
   ```
   GET http://<servidor>/requests
   ```

3. Para o serviço de respostas de feedback:
   ```
   GET http://<servidor>/responses
   ```

Substitua `<servidor>` pelo endereço IP ou nome do host onde o Nginx está rodando.

# Glossário

- **Nginx**: Um servidor web que pode ser usado como servidor reverso, balanceador de carga, proxy de e-mail e cache HTTP.
- **upstream**: Bloco de configuração no Nginx que define um grupo de servidores backend para onde as requisições podem ser proxy-passadas.
- **proxy_pass**: Diretiva do Nginx que encaminha solicitações para os servidores backend especificados no bloco `upstream`.
- **rewrite**: Diretiva do Nginx usada para reescrever URLs antes de encaminhar a requisição para o backend.
- **Servidor Reverso**: Um tipo de servidor que recupera recursos em nome de um cliente de um ou mais servidores backend.