from telebot.types import Message
from loader import bot


@bot.message_handler(commands=['списокЮбилейный'])
def bot_lucky_list(message: Message):
    pass
