from telebot.types import Message
from loader import bot
from config_data.config import ADMIN_IDS
from database.commands import get_moderator_id


@bot.message_handler(commands=['start'])
def bot_start(message: Message):
    """
    Хендлер, который приветствует пользователя и подсказывает, что делать при команде /start.
    :param Message message: /start
    :return: None
    """
    moderator_ids = get_moderator_id()

    if (message.chat.id in moderator_ids) or (str(message.from_user.id) in ADMIN_IDS):
        bot.send_message(chat_id=message.chat.id,
                         text=f"Привет, {message.from_user.full_name}!\n"
                              f"Чтобы узнать, что я могу, введи команду /help\n\n"
                              f"При первом запуске администратору следует выставить настройки админки по "
                              f"команде /adminsetup в личном чате со мной (доступ есть у пользователей с "
                              f"такими user_id:\n{ADMIN_IDS})")
