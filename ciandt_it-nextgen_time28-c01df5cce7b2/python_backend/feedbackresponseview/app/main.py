from contextlib import asynccontextmanager

from fastapi import FastAPI
from dotenv import load_dotenv

load_dotenv()

from .models import db

app = FastAPI()


@asynccontextmanager
async def lifespan(app):
    db.connect()
    yield
    db.close()


@app.get("/health")
def health():
    return {"status": "healthy"}
