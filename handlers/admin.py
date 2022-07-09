from telebot.types import Message
from loader import bot


@bot.message_handler(commands=['adminshow'])
def bot_admin_show(message: Message):
    # Здесь бот выводит текущие настройки админки
    pass


@bot.message_handler(commands=['adminsetup'])
def bot_admin_setup(message: Message):
    # Здесь можно настроить админку
    pass
