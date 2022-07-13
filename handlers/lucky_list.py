from telebot.types import Message
from loader import bot
import database.commands as usersbase
import datetime


@bot.message_handler(commands=['luckylist'])
def bot_lucky_list(message: Message):
    # usersbase.MODERATOR_ID = message.chat.id
    winners = usersbase.select_lucky(message.chat.id)
    # print(message.chat.id)
    # print(winners)
    for winner in winners:
        dtime = datetime.datetime.strptime(winner[4], '%Y-%m-%d %H:%M:%S.%f').strftime('%d.%m.%y %H:%M')
        if winner[5] == 1:
            bot.send_message(chat_id=message.chat.id, text=f'\U0001F451\U0001F451\U0001F451  ★★★ "{winner[0]}"  \U0001F464  {winner[1]}'
                                                           f'(@{winner[2]})\n\U0001F522  {winner[3]}  \U0001F550 	{dtime}')
        else:
            bot.send_message(chat_id=message.chat.id, text=f'\U0001F389  "{winner[0]}"  \U0001F464  {winner[1]}  '
                                                       f'(@{winner[2]})\n\U0001F522  {winner[3]}  \U0001F550 	{dtime}')
