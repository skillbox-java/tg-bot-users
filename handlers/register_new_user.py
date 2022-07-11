from loader import bot
from database.commands import insert
import datetime
from database.commands import winner_check



@bot.message_handler(content_types=['new_chat_members'])
def handler_new_member(message):
    count = bot.get_chat_members_count(message.chat.id)
    user_number = message.from_user.id

    if not message.from_user.is_bot and not winner_check(user_number): # тут будет еще проверка count % 500 == 0

        bot.send_message(message.chat.id, 'Не бот и отсутствует в списке победителей')
        insert(
            nickname=message.from_user.username, user_name=message.from_user.first_name,
            user_number=message.from_user.id, dtime=datetime.datetime.now(),
            chat_id=message.chat.id, is_winer=message.from_user.id
        )
    else:
        bot.send_message(message.chat.id, f'Есть в списке победителей{winner_check(user_number)}')