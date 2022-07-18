from datetime import datetime

from peewee import (SqliteDatabase,
                    Model,
                    CharField,
                    IntegerField,
                    DateTimeField,
                    ForeignKeyField,
                    BooleanField
                    )

from config_data.config import BASE_DIR

database_path = BASE_DIR / 'database' / 'users.db'
db = SqliteDatabase(f'{database_path}')


class BaseModel(Model):
    class Meta:
        database = db


class User(BaseModel):
    group_name = CharField()
    user_id = IntegerField(unique=True)
    user_name = CharField()
    user_mention = CharField()
    current_time = DateTimeField(default=datetime.now)


class TechInfo(BaseModel):
    owner = ForeignKeyField(User)
    user_counter = IntegerField()
    message_id = IntegerField(null=True)
    deleted = BooleanField(default=False)
    congratulated = BooleanField(default=False)
