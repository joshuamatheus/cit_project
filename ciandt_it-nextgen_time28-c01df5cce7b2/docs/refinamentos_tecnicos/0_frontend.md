# Frontend com NextJS e Material UI

Este projeto é uma aplicação frontend utilizando Next.js, React e Material UI. Abaixo estão as instruções para configurar, executar e desenvolver neste projeto.

## Scripts Disponíveis

No projeto, você pode executar os seguintes comandos:

- `dev`: Inicia o servidor de desenvolvimento utilizando o Next.js com Turbopack.
  ```bash
  npm run dev
  ```
- `build`: Gera uma build otimizada para produção.
  ```bash
  npm run build
  ```
- `start`: Inicia o servidor com a aplicação previamente construída.
  ```bash
  npm run start
  ```
- `lint`: Executa o linter para identificar e corrigir problemas de estilo de código.
  ```bash
  npm run lint
  ```
- `test`: Executa os testes unitários da aplicação.
  ```bash
  npm run test
  ```

## Dependências

### Principais

- `@emotion/cache`, `@emotion/react`, `@emotion/styled`: Utilizados para estilização CSS-in-JS.
- `@mui/icons-material`, `@mui/material`, `@mui/material-nextjs`: Componentes de interface do usuário.
- `next`: Framework de React para produção.
- `react`, `react-dom`: Biblioteca principal de componentes para construção de interfaces.
  
### Desenvolvimento

- `@eslint/eslintrc`: Configuração para ESLint.
- `@testing-library/dom`, `@testing-library/react`: Bibliotecas para testes de componentes React.
- `@types/node`, `@types/react`, `@types/react-dom`: Tipagem TypeScript para Node.js e React.
- `@vitejs/plugin-react`: Plugin para integrar React com Vite.
- `eslint`, `eslint-config-next`: Ferramentas para análise estática de código.
- `jsdom`: Implementação do DOM para Node.js, utilizada em testes.
- `typescript`: Linguagem de programação que adiciona tipagem estática ao JavaScript.
- `vite-tsconfig-paths`: Suporte para caminhos de importação no TypeScript com Vite.
- `vitest`: Framework de testes.

## Exemplo de Criação de Endpoint com Next.js

Para criar um novo endpoint, adicione um arquivo na pasta `pages/api`. Por exemplo, para criar um endpoint `hello`, crie o arquivo `pages/api/hello.js` com o seguinte conteúdo:

```javascript
export default function handler(req, res) {
  res.status(200).json({ message: 'Hello, world!' });
}
```

A partir de agora, ao acessar `/api/hello`, você receberá a resposta JSON `{ "message": "Hello, world!" }`.

## Exemplo de Teste com Testing Library e Vitest

Para criar testes, crie um arquivo com a extensão `.test.js` ou `.test.ts`. A seguir, um exemplo de teste simples utilizando Vitest e Testing Library:

```javascript
import { render, screen } from '@testing-library/react';
import Home from '../pages/index';

test('renders welcome message', () => {
  render(<Home />);
  const linkElement = screen.getByText(/Bem-vindo ao Next.js/i);
  expect(linkElement).toBeInTheDocument();
});
```

## Glossário

- **Next.js**: Um framework React para a construção de aplicações web, que oferece funcionalidades como renderização do lado do servidor e geração de sites estáticos.
- **React**: Biblioteca JavaScript para construção de interfaces de usuário.
- **Material UI**: Biblioteca de componentes React que implementa os princípios do Material Design.
- **Turbopack**: Ferramenta de bundling otimizada utilizada pelo Next.js para acelerar o processo de desenvolvimento.
- **ESLint**: Ferramenta de linting para identificar e corrigir problemas no código JavaScript.
- **Testing Library**: Conjunto de utilitários para testar componentes de interface de usuário de maneira que simule o comportamento real do usuário.
- **Vitest**: Framework de testes que utiliza o Vite para uma experiência de desenvolvimento rápida.
- **TypeScript**: Superset do JavaScript que adiciona tipagem estática ao código, permitindo detectar erros em tempo de compilação.
- **CSS-in-JS**: Técnica de estilização onde o CSS é escrito dentro de arquivos JavaScript, geralmente associado a bibliotecas como Emotion e Styled Components.