from telebot.types import Message
from loader import bot
from database.commands import get_all_moderator_id, select_lucky
import datetime


@bot.message_handler(commands=['luckylist'])
def bot_lucky_list(message: Message) -> None:
    """"
    Хендлер, который обрабатывает команду /luckylist по выводу всех зарегитрированных юбилейных пользователей.
    :param Message message: /luckylist
    :return: None
    """
    moderator_ids = get_all_moderator_id()

    if message.chat.id in moderator_ids:
        winners = select_lucky(moderator_id=message.chat.id)

        for winner in winners:
            dtime = datetime.datetime.strptime(winner[4], '%Y-%m-%d %H:%M:%S.%f').strftime('%d.%m.%y %H:%M')
            if winner[5] == 1:
                bot.send_message(chat_id=message.chat.id,
                                 text=f'\U0001F451\U0001F451\U0001F451 "{winner[0]}"  \U0001F464  {winner[1]} '
                                      f'(@{winner[2]})\n\U0001F522  {winner[3]}  \U0001F550 	{dtime}')
            else:
                bot.send_message(chat_id=message.chat.id,
                                 text=f'\U0001F389  "{winner[0]}"  \U0001F464  {winner[1]} '
                                      f'(@{winner[2]})\n\U0001F522  {winner[3]}  \U0001F550 	{dtime}')
