import os
from dotenv import load_dotenv, find_dotenv

if not find_dotenv():
    exit('Переменные окружения не загружены т.к отсутствует файл .env')
else:
    load_dotenv()

BOT_TOKEN = os.getenv('BOT_TOKEN')
ADMIN_IDS = os.getenv('ADMIN_IDS')
HAPPY_NUMBER = 5

DEFAULT_COMMANDS = (
    ('start', "Запустить бота"),
    ('help', "Вывести справку"),
    ('adminsetup', "Установить настройки админки"),
    ('adminshow', "Вывести текущие настройки админки"),
    ('luckylist', "Запросить юбилейный список"),
    ('unceleb', "Вывести последних не поздравленных"),

)
