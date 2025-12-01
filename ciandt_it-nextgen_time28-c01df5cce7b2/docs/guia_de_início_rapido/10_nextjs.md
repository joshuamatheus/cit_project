# Next.JS

O nextjs é um framework que se utiliza de toda a mecânica do react com o adicional que adiciona um sistema de roteamento, compilação e renderização no backend que fazem dele uma ferramenta completa para desenvolvimento web

## 1. **Arquitetura de Páginas**
O Next.js adota uma arquitetura baseada em páginas, onde cada arquivo dentro da pasta `/pages` se torna uma rota na aplicação. Por exemplo:
- `pages/index.js` se torna a rota `/`
- `pages/about.js` se torna a rota `/about`

Essa abordagem de roteamento automático simplifica a estrutura do projeto e facilita a navegação entre diferentes partes da aplicação.

## 2. **Renderização**
O Next.js oferece diferentes métodos de renderização para otimizar o desempenho e a experiência do usuário:

Claro! Vamos detalhar como usar a Renderização do Lado do Servidor (SSR) em aplicações web, especialmente no contexto do Next.js, e como distingui-la da Renderização do Lado do Cliente (Client-Side Rendering - CSR).

## Uso da Renderização do Lado do Servidor (SSR)

### 1. **Definição do SSR**
A Renderização do Lado do Servidor (SSR) refere-se à técnica onde a geração do HTML da página ocorre no servidor, permitindo que o conteúdo seja enviado ao cliente já renderizado. Isso é especialmente útil para SEO e para melhorar a experiência do usuário, já que o conteúdo está disponível imediatamente quando a página é carregada.

### 2. **Como Usar SSR no Next.js**

Para implementar SSR no Next.js, você utiliza a função `getServerSideProps`. Esta função é exportada de um componente de página e é chamada pelo Next.js no lado do servidor antes de renderizar a página.

#### Exemplo de SSR

```javascript
// pages/exemplo.js
export async function getServerSideProps(context) {
  // Aqui você pode buscar dados de uma API ou banco de dados
  const res = await fetch('https://api.exemplo.com/dados');
  const dados = await res.json();

  // Retorne os dados como props para o componente
  return { props: { dados } };
}

const Exemplo = ({ dados }) => {
  return (
    <div>
      <h1>Dados Carregados do Servidor</h1>
      <pre>{JSON.stringify(dados, null, 2)}</pre>
    </div>
  );
};

export default Exemplo;
```

### 3. **Fluxo de Funcionamento do SSR**

- Quando um usuário acessa a rota `/exemplo`, o Next.js chama a função `getServerSideProps`.
- O servidor faz a requisição para a API, obtém os dados e os retorna como props.
- O Next.js renderiza a página no servidor com os dados recebidos e envia o HTML completo ao cliente.
- O cliente vê a página já renderizada, melhorando o tempo de carregamento inicial e a experiência do usuário.

## Distinguir SSR de Client-Side Rendering (CSR)

### 1. **Definição do CSR**
A Renderização do Lado do Cliente (CSR) é uma técnica em que a maior parte do processamento para renderizar a página é feita no navegador do usuário. Isso significa que o HTML inicial enviado pelo servidor é mínimo, e o conteúdo é carregado dinamicamente através de chamadas de API após a página ser carregada.

### 2. **Como Funciona o CSR**

- O servidor envia um HTML básico com um arquivo JavaScript que contém a lógica da aplicação.
- O JavaScript é baixado e executado no navegador, que faz requisições adicionais para buscar dados e renderizar a interface.
- O conteúdo é exibido somente após o JavaScript ser executado, o que pode resultar em uma experiência de usuário menos eficiente, especialmente em conexões lentas.

#### Exemplo de CSR

```javascript
// pages/csr.js
import { useEffect, useState } from 'react';

const Csr = () => {
  const [dados, setDados] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      const res = await fetch('https://api.exemplo.com/dados');
      const data = await res.json();
      setDados(data);
    };
    
    fetchData();
  }, []);

  return (
    <div>
      <h1>Dados Carregados do Cliente</h1>
      {dados ? <pre>{JSON.stringify(dados, null, 2)}</pre> : <p>Carregando...</p>}
    </div>
  );
};

export default Csr;
```

### 3. **Comparação entre SSR e CSR**

| Característica            | SSR                                    | CSR                                      |
|---------------------------|----------------------------------------|------------------------------------------|
| **Onde o HTML é gerado** | No servidor (antes de enviar ao cliente) | No cliente (no navegador após o carregamento) |
| **SEO**                   | Melhor, pois o conteúdo é renderizado antes de ser enviado ao cliente | Pode ser pior, pois motores de busca podem não indexar corretamente conteúdo carregado dinamicamente |
| **Tempo de Carregamento Inicial** | Mais rápido, pois o HTML completo é enviado | Pode ser mais lento, pois o usuário espera o JavaScript ser baixado e executado |
| **Interação e Navegação**| Pode ser mais lenta em navegações subsequentes devido ao carregamento do servidor | Mais rápida após o carregamento inicial, pois a navegação pode ser feita sem recarregar a página |
| **Uso de Recursos**      | Pode consumir mais recursos no servidor devido ao processamento de cada requisição | Pode consumir mais recursos no cliente, pois o processamento é feito no navegador |

## Geração de Sites Estáticos (SSG)
 As páginas são geradas no momento da construção e servidas como arquivos estáticos. Isso é ideal para conteúdo que não muda frequentemente.

```javascript
export async function getStaticProps() {
  // Fetch data here
  return { props: { data } };
}
```

## Geração de Páginas Estáticas com Incremental Static Regeneration (ISR)

Permite a atualização de páginas estáticas após a construção inicial, garantindo que o conteúdo esteja sempre atualizado sem a necessidade de uma nova construção da aplicação.

```javascript
export async function getStaticProps() {
  return {
    props: { data },
    revalidate: 10, // Revalidate every 10 seconds
  };
}
```

### 3. **API Routes**
O Next.js permite que você crie APIs diretamente dentro do projeto, facilitando a construção de endpoints sem a necessidade de um servidor separado. As rotas da API são criadas dentro da pasta `/api`.

```javascript
// pages/api/hello.js
export default function handler(req, res) {
  res.status(200).json({ message: 'Hello World' });
}
```

### 4. **Otimização de Imagens**
O Next.js fornece um componente `<Image />` que otimiza automaticamente as imagens, incluindo recursos como lazy loading e redimensionamento, melhorando o desempenho geral da aplicação.

```javascript
import Image from 'next/image';

function MeuComponente() {
  return <Image src="/imagem.jpg" alt="Minha Imagem" width={500} height={300} />;
}
```

### 5. **Estilização**
O Next.js suporta várias abordagens de estilização, incluindo CSS, CSS Modules e bibliotecas CSS-in-JS. Isso permite que os desenvolvedores escolham a abordagem que melhor se adapta às suas necessidades.

### 6. **Divisão de Código**
O Next.js realiza a divisão automática de código, o que significa que cada página carrega apenas o código necessário para ela. Isso resulta em tempos de carregamento mais rápidos e melhor desempenho.

### 7. **Suporte a TypeScript**
O Next.js oferece suporte nativo ao TypeScript, permitindo que os desenvolvedores escrevam código com tipagem estática, o que pode ajudar a detectar erros em tempo de desenvolvimento.

### 8. **Internacionalização**
O Next.js facilita a criação de aplicações multilíngues com suporte a rotas e conteúdos localizados, permitindo que você atenda a uma audiência global.

### 9. **Deploy Simples**
O Next.js é otimizado para ser implantado de maneira simples, especialmente na plataforma Vercel, que é a criadora do Next.js. Ele permite um fluxo de trabalho contínuo com integração fácil a repositórios Git.

### 10. **Performance e SEO**
O Next.js é projetado para oferecer alto desempenho e otimização para SEO (Search Engine Optimization). A renderização no servidor e a geração de páginas estáticas garantem que os motores de busca consigam indexar o conteúdo da aplicação facilmente.