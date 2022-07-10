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
    ('help', "Вывести справку"),
)

USER_THRESHOLD = 4
ADMINISTRATORS_GROUP_ID = [-541752986, ]
USERS_GROUP = [-798504460, ]
