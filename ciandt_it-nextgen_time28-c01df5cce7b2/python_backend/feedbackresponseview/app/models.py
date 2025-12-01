from peewee import Model, CharField, TextField, ForeignKeyField, PostgresqlDatabase

import os

database_url = os.getenv("DATABASE_URL", "postgres://user:password@db:5432/mydatabase")
db = PostgresqlDatabase(database_url)


class FeedbackResponse():
    pass