import os

from dotenv import load_dotenv, find_dotenv

if not find_dotenv():
    exit('Переменные окружения не загружены')
else:
    load_dotenv()

BOT_TOKEN = os.getenv('BOT_TOKEN')
COMMANDS = (

)

USER_THRESHOLD = 4
ADMINISTRATORS_GROUP_ID = [-541752986, ]
USERS_GROUP = [-798504460, ]
