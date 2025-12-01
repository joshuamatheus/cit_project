# TDD - Test Driven Development

![alt text](tdd.png)

**TDD**, ou **Desenvolvimento Orientado a Testes** (Test-Driven Development), é uma prática de desenvolvimento de software que enfatiza a criação de testes automatizados antes da implementação do código funcional. O TDD é baseado na ideia de que os testes devem ser uma parte fundamental do processo de desenvolvimento, e não apenas uma verificação final.

## Princípios do TDD

1. **Escreva um Teste**: Antes de escrever qualquer código, você deve escrever um teste que defina uma função ou melhoria desejada. Este teste deve falhar inicialmente, pois a funcionalidade ainda não foi implementada.

2. **Implemente a Funcionalidade**: Após escrever o teste, você deve implementar a mínima quantidade de código necessária para fazê-lo passar.

3. **Refatore**: Uma vez que o teste passa, você pode refatorar o código para melhorar sua qualidade, mantendo os testes verdes (passando). Refatoração é uma parte crítica do TDD, pois permite que você melhore o design do código sem alterar seu comportamento.

## Benefícios do TDD

- **Feedback Rápido**: Permite detectar falhas rapidamente, já que os testes são executados frequentemente.
- **Código Mais Limpo e Manutenível**: Como o código é frequentemente refatorado, ele tende a ser mais limpo e mais fácil de manter.
- **Documentação Viva**: Os testes servem como uma forma de documentação do comportamento do sistema.

## Ciclo Red-Green-Blue

O ciclo **Red-Green-Blue** é uma abordagem visual e prática para implementar o TDD. Cada cor representa uma fase no processo de desenvolvimento:

1. **Red (Vermelho)**:
   - Escreva um teste que valide uma nova funcionalidade. No início, o teste deve falhar, indicando que a funcionalidade ainda não foi implementada. Essa fase é importante porque confirma que o teste está, de fato, verificando algo significativo.

2. **Green (Verde)**:
   - Implemente a funcionalidade mínima necessária para que o teste passe. O objetivo aqui é garantir que a implementação atenda ao teste. Assim que o teste passa, você pode dizer que a funcionalidade está "verde".

3. **Blue (Azul)**:
   - Refatore o código. O objetivo é melhorar a estrutura e a legibilidade do código, garantindo que todos os testes ainda passem. O termo "azul" pode não ser comum; muitas vezes, a etapa de refatoração é simplesmente chamada de "refatoração", mas a ideia é que agora você está em um estado "estável" e "limpo".

TDD e o ciclo Red-Green-Blue são práticas poderosas que podem melhorar significativamente a qualidade do código e a eficiência do desenvolvimento. Ao escrever testes antes de implementar funcionalidades, os desenvolvedores podem garantir que o código atenda aos requisitos desde o início e que permaneça robusto e flexível à medida que evolui. Essa abordagem não apenas melhora a qualidade do software, mas também ajuda a construir uma mentalidade de qualidade e responsabilidade entre os desenvolvedores. 

## Exemplo de uso do TDD

Abordaremos a implementação de um sistema de gerenciamento de personagens em um jogo, onde os personagens podem ter atributos como força de ataque e defesa, além de equipar armaduras, armas e capacetes. O sistema será desenvolvido inicialmente de forma funcional e, em seguida, refatorado para uma abordagem orientada a objetos (OOP).

### 1. Criando o Teste

Usaremos o framework `pytest` para criar testes unitários que verificarão se o sistema de personagens funciona conforme o esperado. Os testes incluirão:

- Verificação da criação de um personagem.
- Adição de armaduras, armas e capacetes.
- Cálculo correto da força de ataque e defesa.

_tests.py_
```python
import pytest

from character import *

def test_character_creation():
    create_character(1, 'Guerreiro Valente')
    stats = get_stats(1)
    assert stats['nome'] == 'Guerreiro Valente'
    assert stats['ataque'] == 10
    assert stats['defesa'] == 5

def test_add_armor():
    create_character(2, 'Cavaleiro Forte')
    add_armor(2, 1)  # Adiciona Cota de Malha
    stats = get_stats(2)
    assert stats['defesa'] == 8  # 5 base + 3 da armadura

def test_add_weapon():
    create_character(3, 'Lutador Rápido')
    add_weapon(3, 2)  # Adiciona Machado
    stats = get_stats(3)
    assert stats['ataque'] == 15  # 10 base + 5 da arma

def test_add_helmet():
    create_character(4, 'Guerreiro Blindado')
    add_helmet(4, 2)  # Adiciona Capacete de Aço
    stats = get_stats(4)
    assert stats['defesa'] == 8  # 5 base + 3 do capacete
```

### Rodar Para Falhar <span style="color:red">(RED)</span>

É uma boa prática rodar os testes antes de implementar qualquer funcionalidade. Isso garante que, se houver alguma falha nos testes, saberemos que precisa ser corrigida. Portanto, ao executar o comando `pytest`, devemos garantir que todos os testes falhem inicialmente, indicando que a implementação não está completa.

## 2. Implementação Funcional

Abaixo está a implementação funcional do sistema de gerenciamento de personagens:

_character.py_
```python
# Implementação Funcional

personagens = {}

def create_character(id_personagem, nome):
    """Cria um novo personagem com atributos iniciais."""
    personagens[id_personagem] = {
        'nome': nome,
        'ataque': 10,  # Força de ataque base
        'defesa': 5,   # Força de defesa base
        'armaduras': [],
        'armas': [],
        'capacetes': []
    }

def add_armor(id_personagem, id_armor):
    armor_stats = {
        1: {'nome': 'Cota de Malha', 'bonus_defesa': 3},
        2: {'nome': 'Armadura de Couro', 'bonus_defesa': 2},
        3: {'nome': 'Armadura Pesada', 'bonus_defesa': 5}
    }
    
    if id_personagem in personagens and id_armor in armor_stats:
        armor = armor_stats[id_armor]
        personagens[id_personagem]['defesa'] += armor['bonus_defesa']
        personagens[id_personagem]['armaduras'].append(armor['nome'])

def add_weapon(id_personagem, id_weapon):
    weapon_stats = {
        1: {'nome': 'Espada Curta', 'bonus_ataque': 4},
        2: {'nome': 'Machado', 'bonus_ataque': 5},
        3: {'nome': 'Lança', 'bonus_ataque': 3}
    }
    
    if id_personagem in personagens and id_weapon in weapon_stats:
        weapon = weapon_stats[id_weapon]
        personagens[id_personagem]['ataque'] += weapon['bonus_ataque']
        personagens[id_personagem]['armas'].append(weapon['nome'])

def get_stats(id_personagem):
    """Retorna a força de ataque e defesa do personagem."""
    if id_personagem in personagens:
        return personagens[id_personagem]
    return None
```

### Rodar o teste para passar <span style="color:green">(GREEN)</span>

Rodar o teste a essa altura vai ocasionar que o mesmo passe com sucesso, estamos na fase em que já temos uma versão funcional do código

## 3. Refatoração para Objetos

Após a implementação funcional, o próximo passo é refatorar o código para uma abordagem orientada a objetos. A refatoração envolverá criar uma classe `Personagem` que encapsula os atributos e métodos necessários para manipular os personagens.

### Implementação Orientada a Objetos

```python

@dataclass
class Armadura:
    nome: str
    bonus_defesa: str

@dataclass
class Arma:
    nome: str
    bonus_ataque: str

@dataclass
class Personagem:
    id_personagem: int
    nome: str
    forca_ataque: int = 10
    forca_defesa: int = 5
    armaduras:list[Armadura] = []
    armas:list[Arma] = []

    def add_armadura(self, armadura: Armadura):
        self.armaduras.append(armadura)

    def add_arma(self, arma: Arma):
        self.armas.append(arma)

    @property
    def ataque(self):
        return sum([self.forca_ataque, *[a.bonus_ataque for a in self.armas]])

    @property
    def defesa(self):
        return sum([self.forca_ataque, *[a.bonus_defesa for a in self.armaduras]])

    def get_stats(self):
        return {
            'nome': self.nome,
            'ataque': self.ataque,
            'defesa': self.defesa
        }

personagens = {}

aramaduras = {
    1: Armadura('Cota de Malha', 3),
    2: Armadura('Armadura de Couro', 2),
    3: Armadura('Armadura Pesada', 5),
}

armas = {
    1: Arma('Espada Curta', 4),
    2: Arma('Machado', 5),
    3: Arma('Lança', 3),
}

def create_character(id_personagem, nome):
    personagems[id_personagem] = Personagem(1, nome)

def add_armor(id_personagem, id_armor):
    if id_personagem in personagens and id_armor in armor_stats:
        armor = armor_stats[id_armor]
        personagems[is_personagem].add_armor(armor)

def add_weapon(id_personagem, id_weapon):
    if id_personagem in personagens and id_weapon in weapon_stats:
        weapon = weapon_stats[id_weapon]
        personagens[id_personagem].add_weapon(weapon)

def get_stats(id_personagem):
    """Retorna a força de ataque e defesa do personagem."""
    if id_personagem in personagens:
        return personagens[id_personagem].get_stats()

    return None

```

### Refatorando sem mudar os testes <span style="color:blue">(BLUE)</span>

A refatoração para OOP não requer mudanças nos testes, pois a assinatura dos métodos e a lógica de manipulação de personagens permanecem as mesmas. A estrutura dos testes já criada para a implementação funcional funcionará com a nova implementação orientada a objetos, garantindo que o comportamento do sistema permaneça consistente.

## Conclusão

Com TDD, abordamos a implementação de um sistema de gerenciamento de personagens em um jogo, desde a implementação funcional até a refatoração orientada a objetos. Garantimos que a transição para OOP não afetasse os testes existentes, o que é crucial para a manutenção e evolução do código. Essa abordagem modular e testável facilita o desenvolvimento futuro e a manutenção do sistema.