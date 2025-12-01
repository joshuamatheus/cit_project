# FastAPI, Peewee e Building Blocks

Com fastapi e peewee se torna bastante simples de implementar os padrões táticos requeridos nesse projeto

## 1. Entity

As *Entities* representam objetos do domínio que possuem uma identidade única. No contexto do Peewee, as entidades são definidas como modelos que se conectam a uma tabela no banco de dados.

### Exemplo:
```python
from peewee import Model, CharField, SqliteDatabase

# Conexão com o banco de dados
db = SqliteDatabase('users.db')

class User(Model):
    name = CharField()
    email = CharField(unique=True)

    class Meta:
        database = db  # Define o banco de dados associado ao modelo
```

## 2. Repository

O *Repository* atua como um intermediário entre a aplicação e a camada de persistência de dados. Ele fornece métodos para manipular as entidades.

### Exemplo:
```python
class UserRepository:
    @staticmethod
    def create_user(name: str, email: str) -> User:
        user = User.create(name=name, email=email)
        return user

    @staticmethod
    def get_user(user_id: int) -> User:
        return User.get_or_none(User.id == user_id)

    @staticmethod
    def get_all_users() -> list:
        return list(User.select())

    @staticmethod
    def delete_user(user_id: int) -> None:
        user = User.get_or_none(User.id == user_id)
        if user:
            user.delete_instance()
```

## 3. Service

O *Service* encapsula a lógica de negócios da aplicação, orquestrando as interações entre as entidades e os repositórios.

### Exemplo:
```python
class UserService:
    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def create_user(self, name: str, email: str) -> User:
        return self.user_repository.create_user(name, email)

    def get_user(self, user_id: int) -> User:
        return self.user_repository.get_user(user_id)

    def get_all_users(self) -> list:
        return self.user_repository.get_all_users()

    def delete_user(self, user_id: int) -> None:
        self.user_repository.delete_user(user_id)
```

## 4. Controller

O *Controller* lida com as requisições do usuário, coordenando a interação entre a interface do usuário e os serviços. O FastAPI facilita a criação de APIs com rotas e manipulação de dados.

### Exemplo:
```python
from fastapi import FastAPI, HTTPException

app = FastAPI()
user_repository = UserRepository()
user_service = UserService(user_repository)

@app.post("/users/")
def create_user(name: str, email: str):
    user = user_service.create_user(name, email)
    return {"id": user.id, "name": user.name, "email": user.email}

@app.get("/users/{user_id}")
def get_user(user_id: int):
    user = user_service.get_user(user_id)
    if user:
        return {"id": user.id, "name": user.name, "email": user.email}
    raise HTTPException(status_code=404, detail="User not found")

@app.get("/users/")
def get_all_users():
    users = user_service.get_all_users()
    return [{"id": user.id, "name": user.name, "email": user.email} for user in users]

@app.delete("/users/{user_id}")
def delete_user(user_id: int):
    user_service.delete_user(user_id)
    return {"message": "User deleted successfully"}
```

## 5. TDD

Vamos usar o TestClient fornecido pelo FastApi. Esta abordagem permite testar os endpoints do FastAPI de forma integrada, validando a funcionalidade do aplicativo sem a necessidade de mocks, além de garantir que o comportamento do sistema esteja conforme o esperado.

```python
import pytest
from fastapi.testclient import TestClient
from your_module import app, UserRepository, UserService, User  # Ajuste a importação conforme necessário

# Inicialização do TestClient
client = TestClient(app)

# Supondo que você tenha um UserRepository e UserService apropriados
@pytest.fixture(scope="module")
def setup_users():
    # Limpeza e configuração inicial do repositório de usuários
    user_repository = UserRepository()
    user_service = UserService(user_repository)
    
    # Adicionando usuários para teste
    user_service.create_user("Alice", "alice@example.com")
    user_service.create_user("Bob", "bob@example.com")

    yield  # Permite que os testes sejam executados

    # Limpeza após os testes
    user_repository.clear()  # Suponha que você tenha um método para limpar os dados

def test_create_user():
    response = client.post("/users/", json={"name": "Charlie", "email": "charlie@example.com"})
    assert response.status_code == 200
    data = response.json()
    assert "id" in data
    assert data["name"] == "Charlie"
    assert data["email"] == "charlie@example.com"

def test_get_user(setup_users):
    response = client.get("/users/1")  # Assume que o ID do primeiro usuário é 1
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "Alice"
    assert data["email"] == "alice@example.com"

def test_get_user_not_found():
    response = client.get("/users/999")  # ID que não existe
    assert response.status_code == 404
    assert response.json() == {"detail": "User not found"}

def test_get_all_users(setup_users):
    response = client.get("/users/")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 2  # Espera-se que existam 2 usuários criados no setup
    assert data[0]["name"] == "Alice"
    assert data[1]["name"] == "Bob"

def test_delete_user(setup_users):
    response = client.delete("/users/1")  # Deletar o usuário com ID 1
    assert response.status_code == 200
    assert response.json() == {"message": "User deleted successfully"}

    # Verificar se o usuário foi realmente deletado
    response = client.get("/users/1")
    assert response.status_code == 404
```
