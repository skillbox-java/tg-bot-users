from telebot.types import Message
from config_data.config import DEFAULT_COMMANDS
from loader import bot
from database.commands import get_moderator_id


@bot.message_handler(commands=['help'])
def bot_help(message: Message):
    """
    Хендлер, который выводит все доступные боту команды при команде /help.
    :param Message message: /help
    :return: None
    """
    moderator_ids = get_moderator_id()

    if message.chat.id in moderator_ids:
        text = [f'/{command} - {desk}' for command, desk in DEFAULT_COMMANDS]
        bot.send_message(chat_id=message.chat.id,
                         text='\n'.join(text))
