# Api Gateway implementado com Node e Express

Este projeto implementa um servidor proxy utilizando Express e `http-proxy-middleware` para encaminhar requisições para os serviços do backend. Além disso, ele utiliza JWTs (JSON Web Tokens) para autenticar e autorizar requisições.

## Estrutura do Projeto

- `app.ts`: Arquivo principal que configura o servidor Express, middleware de autenticação JWT e a lógica de proxy.
- `index.ts`: Ponto de entrada da aplicação que inicia o servidor.
- `app.spec.ts`: Arquivo de testes utilizando `supertest` e `jest` para verificar o comportamento do proxy e autenticação.

## Funcionalidades

1. **Autenticação JWT**: O middleware verifica se um JWT válido é fornecido no header `Authorization`. Se o token for inválido ou ausente, a requisição é bloqueada.
  
2. **Proxy Middleware**: As requisições para o caminho `/users` são encaminhadas para o serviço de gerenciamento de usuários. Durante o proxy, o header `x-user` é adicionado, contendo informações extraídas do JWT.

3. **Testes Automatizados**: O projeto inclui testes para verificar o comportamento de autenticação e proxy.

## Configuração

### Variáveis de Ambiente

- `SECRET_KEY`: Chave secreta para assinar e verificar JWTs. Deve ser definida no arquivo `.env`.
- `USER_MANAGEMENT_API`: URL do serviço de gerenciamento de usuários.
- `PORT`: Porta em que o servidor Express irá rodar.

### Iniciando o Servidor

1. Instale as dependências com `npm install`.
2. Execute o servidor com `npm start`.

### Testando

Os testes são realizados utilizando `jest` e `supertest`. Para rodar os testes, utilize:

```bash
npm test
```

## Exemplos de Uso

### Requisição com JWT Válido

```bash
curl -H "Authorization: Bearer <token_jwt_valido>" http://localhost:<PORT>/users
```

### Requisição sem JWT

```bash
curl http://localhost:<PORT>/users
```

**Nota**: A requisição sem um JWT válido retornará um erro `403` ou `401`.

## Adicionando Novos Endpoints

Para adicionar novos endpoints, siga estas etapas:

1. **Defina o middleware**: Utilize o middleware JWT existente para proteger novas rotas, ou crie um novo caso necessário.
   
2. **Configure o Proxy**: Use o `createProxyMiddleware` para definir novas regras de proxy. Exemplo:

    ```typescript
    app.use('/novo-endpoint', createProxyMiddleware({
        target: 'http://novo-servico:8080',
        changeOrigin: true,
        on: proxyEvents,
        logger: console
    }));
    ```

3. **Teste o Endpoint**: Crie testes utilizando `supertest` no arquivo `app.spec.ts` para verificar o comportamento.

## Exemplo de Teste com TDD

Para criar um teste baseado em TDD, siga este exemplo:

```typescript
it('deve retornar 404 para rota inexistente', async () => {
    const response = await request(app).get('/rota-inexistente');
    expect(response.status).toBe(404);
});
```

## Glossário

- **JWT (JSON Web Token)**: Um padrão de token aberto que permite a transmissão segura de informações entre partes como um objeto JSON.
- **Middleware**: Funções que têm acesso ao objeto de requisição (req), ao objeto de resposta (res) e à próxima função middleware no ciclo de solicitação-resposta.
- **Proxy**: Um intermediário para requisições de cliente que busca recursos de outros servidores.
- **Express**: Um framework para Node.js que fornece um conjunto robusto de recursos para aplicativos web e móveis.
- **`http-proxy-middleware`**: Um middleware para Node.js que age como um proxy reverso, roteando requisições para diferentes servidores.
- **`supertest`**: Uma biblioteca para testar aplicativos HTTP usando Node.js.
- **`dotenv`**: Um módulo que carrega variáveis de ambiente de um arquivo `.env` para `process.env`.