# Figma to Code

O **Figma to Code** é uma poderosa ferramenta do Flow que permite criar componentes de um app baseado em seu design no Figma. Nosso objetivo é que todos os times utilizem essa ferramenta desde a primeira sprint, tornando a experiência de desenvolvimento ainda mais ágil.

## Como funciona

1. O desenvolvedor deve seguir as instruções para habilitar o Figma to Code em seu tenant para o Flow Chat: **[Figma to Code - DOCS]**
2. Localizar um componente no Figma que deseja desenvolver.
3. Copiar a "URL de seleção" do componente:
   - Pressione o botão direito do mouse sobre o componente desejado.
   - Clique em **"Copy as"**.
   - Selecione a opção **"URL de seleção"**.

### Gerando o prompt para o Flow Chat

Após configurar o Figma to Code e copiar o link do componente desejado, basta gerar o prompt para o Flow Chat. Exemplo:

```
[COLAR O LINK] - Crie este formulário de login utilizando Next.js, MUI e TypeScript
```

## Considerações Importantes

- O **Figma to Code** é um **acelerador** para o desenvolvimento, mas o código gerado **não será 100% do código final**. O desenvolvedor precisará revisar e ajustar conforme necessário.
- Para obter melhores resultados, evite gerar telas inteiras de uma só vez.
- Gere componentes menores de forma incremental para maior precisão e melhor componentização.
- Seguir os princípios de **componentização granular** é essencial. Caso a IA gere um componente muito grande, será necessário dividi-lo em partes menores logicamente organizadas.

Essa abordagem garantirá um desenvolvimento mais eficiente e modular, facilitando a manutenção e escalabilidade do projeto.