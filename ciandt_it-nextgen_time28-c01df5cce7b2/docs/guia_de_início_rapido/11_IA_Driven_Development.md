# IA Driven Development

Para todas as técnicas sendo passadas nesse bootcamp, existem melhores formas de se solicitá-las a IA, vamos descorrer de forma básica cada uma delas

## Terminologia Técnica**

<span style="color:green">O uso de termos técnicos demonstra um entendimento do domínio</span>, o que permite que a resposta seja mais técnica e precisa, abordando aspectos que são relevantes para desenvolvedores familiarizados com essas tecnologias.
É **muitíssimo importante ter o conhecimento técnico embasado** tanto para pedir algo a AI quanto para avaliar a entrega que ela fez

## Testando (TDD)

### React / Next

**Exemplo de prompt**:
```
Dado a feature de adicionar um gerente a um usuário através de um combobox autocomplete seguido de uma listagem MUI aonde ficam os selecionados, me dê o teste de integração dessa feature para react usando o vitest (testing library) seguindo os princípios do TDD
```

**O resultado é o seguinte:**

```javascript
// UserManager.test.jsx
import { render, fireEvent, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import UserManager from './UserManager'; // Importando o componente

describe('UserManager', () => {
  it('deve permitir que o usuário adicione um gerente e exibi-lo na lista', async () => {
    render(<UserManager />);

    // Passo 1: Interagir com o combobox
    const input = screen.getByRole('combobox');
    fireEvent.change(input, { target: { value: 'Gerente A' } });

    // Passo 2: Verificar se as sugestões aparecem
    const suggestion = await screen.findByText('Gerente A');
    expect(suggestion).toBeInTheDocument();

    // Passo 3: Selecionar o gerente
    fireEvent.click(suggestion);

    // Passo 4: Verificar se o gerente selecionado aparece na lista
    const selectedManager = screen.getByText('Gerente A');
    expect(selectedManager).toBeInTheDocument();
  });
});
```

Esse prompt é eficiente por várias razões:

1. **Clareza no Objetivo**
Você especificou claramente que deseja um teste de integração para uma feature específica em um aplicativo React. Isso ajuda a focar na tarefa sem ambiguidade.

2. **Contexto Específico**
Ao mencionar a utilização de um "combobox autocomplete" e uma "listagem MUI", é fornecido um contexto técnico que é crucial para entender as bibliotecas e componentes que estão envolvidos. Isso permite que a resposta seja mais relevante e direcionada.

3. **Enfoque no TDD**
Ao solicitar um teste seguindo os princípios do TDD (Test-Driven Development), indica que está interessado em uma abordagem de desenvolvimento que prioriza a qualidade do código e a validação contínua. Isso direciona a resposta para incluir práticas recomendadas e estruturas de teste que se alinham com essa metodologia.

4. **Estrutura do Pedido**
A estrutura do seu pedido é lógica e organizada. Você pediu um teste específico para uma funcionalidade, que é uma prática eficiente em TDD. Isso ajuda a evitar respostas excessivamente gerais e permite que a resposta forneça um exemplo prático e aplicável.

5. **Expectativas Claras**
Ao especificar que deseja um teste de integração, você deixa claro que espera um exemplo que não apenas verifique a funcionalidade isolada, mas que também teste a interação entre componentes, o que é fundamental em aplicações complexas.

6. **Uso de Terminologia Técnica**
O uso de termos como "combobox", "autocomplete" e "MUI" demonstra um entendimento do domínio, o que permite que a resposta seja mais técnica e precisa, abordando aspectos que são relevantes para desenvolvedores familiarizados com essas tecnologias.

**<span style="color:red">Problemas:</span>**

1. No caso do frontend como não temos um banco de dados portanto não temos a possibilidade de ter algo em memória, precisamos de fazer um mock ou ter um stub para ambientes de teste, contudo a IA não nos entregou nenhum dos dois. Isso é porquê pedimos um teste de integração usando a filosofia do TDD, nesse caso deveriamos ter especificado que queriamos a criação desse mock. Isso vai em contradição com o exemplo de backend usado abaixo, que na verdade precisa mencionar que não quer os Mocks. Talvez esse comportamento conflitante se dá pelas fontes de dados pela qual a IA é treinada, aonde a maioria dos projetos backend fazem essa prática errada de mockar tudo e no front os desenvolvedores estão com uma idéia mais alinhada ao TDD de fato em linhas gerais

### Java / Spring

```
Dado a feature de adicionar um gerente a um usuário através de um combobox autocomplete seguido de uma listagem aonde ficam os selecionados, me dê o teste de integração para o backend dessa feature usando o spring test com mockmvc seguindo os princípios do TDD
```

**Resultado desse prompt**

```java
// UserControllerTest.java
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService; // Supondo que você tenha um serviço para manipulação de usuários

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void deveAdicionarGerenteAoUsuario() throws Exception {
        // Dados de entrada
        Long userId = 1L;
        String managerName = "Gerente A";

        // Simulando o comportamento do serviço
        when(userService.addManagerToUser(userId, managerName)).thenReturn(true);

        // Executando a requisição
        mockMvc.perform(post("/api/users/{userId}/managers", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(managerName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Gerente adicionado com sucesso"));
    }
}
```

**<span style="color:red">Problemas**

1. Utilizou um mock do serviço de usuários, efeito contrário a metodologia do TDD aonde os testes devem ser desacoplados da implementação, não se pode deduzir que será usado uma camada de serviços, além disso o teste da funcionalidade como um todo abraçaria o uso de um banco de dados em memória

1. Não criou os tests para o endpoint de busca de gestores, a falta de mencionar que precisa fazer o teste também desse endpoint, muito embora se deve ou não fazer esse teste deveria estar 

## Conclusão

A utilização de Inteligência Artificial (IA) no desenvolvimento de software, especialmente em práticas como o Test-Driven Development (TDD), oferece uma oportunidade valiosa para otimizar e aprimorar a eficiência dos processos de codificação e teste. Como discutido, os prompts bem estruturados e que utilizam terminologia técnica específica são essenciais para obter respostas mais precisas e relevantes da IA. O domínio do conhecimento técnico não apenas melhora a qualidade das solicitações feitas à IA, mas também capacita os desenvolvedores a avaliar criticamente as soluções geradas.

Porém, é crucial estar ciente das limitações que a IA pode apresentar, especialmente em relação à criação de testes que respeitam as melhores práticas do TDD. Exemplos como a necessidade de mocks em um ambiente de frontend ou a utilização de mocks de serviços em testes de backend mostram que a IA pode, por vezes, seguir padrões que não se alinham perfeitamente com as expectativas e filosofias de desenvolvimento. Isso reforça a necessidade de um conhecimento técnico sólido, que possibilita ao desenvolvedor identificar e corrigir eventuais falhas nas respostas geradas pela IA.

Além disso, a compreensão das particularidades de cada stack tecnológica — seja no frontend com React ou no backend com Spring — é fundamental para garantir que as soluções propostas sejam não apenas funcionais, mas também consistentes com as práticas recomendadas de cada contexto.

Em suma, a integração da IA no desenvolvimento de software pode ser extremamente benéfica, mas requer um equilíbrio entre a automação proporcionada pela tecnologia e a expertise humana que garante a qualidade e a eficácia dos resultados. O futuro do desenvolvimento dirigido por IA está promissor, desde que os desenvolvedores continuem a cultivar suas habilidades e a aplicar criticamente o conhecimento técnico em suas interações com essas ferramentas avançadas.
