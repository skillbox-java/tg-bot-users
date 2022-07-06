import os
from dotenv import load_dotenv, find_dotenv

if not find_dotenv():
    exit('Переменные окружения не загружены')
else:
    load_dotenv()

BOT_TOKEN = os.getenv('BOT_TOKEN')
COMMANDS = (

)
