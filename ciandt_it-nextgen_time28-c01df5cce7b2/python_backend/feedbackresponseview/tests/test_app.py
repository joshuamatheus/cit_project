import pytest
from fastapi.testclient import TestClient
from app.main import app
from app.models import db, Feedback

client = TestClient(app)

@pytest.fixture(scope="module")
def setup_db():
    db.connect()
    db.create_tables([Feedback])
    yield
    db.drop_tables([Feedback])
    db.close()

def test_create_feedback(setup_db):
    response = client.post("/feedback/", json={"content": "Test feedback"})
    assert response.status_code == 200
    assert response.json()["content"] == "Test feedback"

def test_get_feedback(setup_db):
    response = client.post("/feedback/", json={"content": "Another feedback"})
    feedback_id = response.json()["id"]
    
    response = client.get(f"/feedback/{feedback_id}")
    assert response.status_code == 200
    assert response.json()["content"] == "Another feedback"