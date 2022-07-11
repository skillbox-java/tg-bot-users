from telebot.types import Message
from loader import bot
import database.commands as usersbase
import datetime
# usersbase.MODERATOR_ID = 1

# winners = usersbase.select_lucky()
@bot.message_handler(commands=['luckylist'])
def bot_lucky_list(message: Message):
    usersbase.MODERATOR_ID = message.chat.id
    winners = usersbase.select_lucky()
    print(winners)
    for winner in winners:
        dtime = datetime.datetime.strptime(winner[4], '%Y-%m-%d %H:%M:%S.%f').strftime('%d.%m.%y %H:%M')

        bot.send_message(chat_id=message.chat.id, text=f'\U0001F389  "{winner[0]}"  \U0001F464  {winner[1]}  '
                                                       f'(@{winner[2]})\n\U0001F522  {winner[3]}  \U0001F550 	{dtime}')
    print(usersbase.MODERATOR_ID, 'luck')


