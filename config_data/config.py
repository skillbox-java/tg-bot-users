import os
from pathlib import Path

from dotenv import load_dotenv, find_dotenv

if not find_dotenv():
    exit('Переменные окружения не загружены')
else:
    load_dotenv()

BASE_DIR = Path(__file__).resolve().parent.parent
BOT_TOKEN = os.getenv('BOT_TOKEN')
COMMANDS = (
    ('anniversary_list', "Получить не поздравленных пользователей"),
    ('users_history', "Показать историю поздравленных\не поздравленных"),
)

USER_THRESHOLD = 3
ADMINISTRATORS_GROUP_ID = [
    # -541752986,
    -721998326,
]
USERS_GROUP = [
    # -798504460,
    -624689956,
]