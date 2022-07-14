from loader import bot
from database.commands import insert, insert2
import datetime
from database.commands import winner_check, select_id_from_users, temp_save, temp_save_bot_message
from telebot import types

# def redirekt_winner_info(user_name, user_id):
#     markup = types.InlineKeyboardMarkup()
#     congratulations = types.InlineKeyboardButton(text='Поздравляем', callback_data='yes')
#     shame = types.InlineKeyboardButton(text='Не повезло', callback_data='no')
#     markup_inline.add(congratulations, shame)
#     bot.send_message(message.chat.id, f'Желаете поздравить пользователя {user_name}?',
#                      reply_markup=markup_inline
#                      )



@bot.message_handler(content_types=['new_chat_members'])
def handler_new_member(message):
    count = bot.get_chat_members_count(message.chat.id)
    user_number=message.from_user.id
    markup = types.InlineKeyboardMarkup(row_width=1)
    congratulations = types.InlineKeyboardButton(text='Поздравляем', callback_data='grac')
    shame = types.InlineKeyboardButton(text='Не поздравляем', callback_data='decline')
    markup.add(congratulations, shame)

    if not message.from_user.is_bot and not winner_check(user_number): # тут будет еще проверка count % 500 == 0
        record_id = select_id_from_users(message.from_user.id)

        insert2(
            nickname=message.from_user.username, user_name=message.from_user.first_name,
            user_id=message.from_user.id, dtime=datetime.datetime.now(),
            chat_id=message.chat.id, is_winner=0)

        temp_save(chat_id=message.chat.id,
                  record_id=select_id_from_users(user_id=message.from_user.id),
                  )



        bot.send_message(message.chat.id, f'Не бот и отсутствует в списке победителей. Что с ним делать?{record_id}',
                         reply_markup=markup)

    else:
        bot.send_message(message.chat.id, f'Есть в списке победителей{winner_check(user_number)}')

@bot.callback_query_handler(func=lambda call: call.data == "grac" or call.data == "decline")
def callback(call):
    if call.message:
        if call.data == 'grac':
            message_id = call.message.message_id

            # temp_save_bot_message(bot_message_id=call.message.message_id)



            bot.delete_message(chat_id=call.message.chat.id, message_id=call.message.message_id)
            bot.send_message(call.message.chat.id, f'Поздравили и добавили в базу. Тест по message id {message_id}')

        else:
            bot.delete_message(chat_id=call.message.chat.id, message_id=call.message.message_id)
            bot.send_message(call.message.chat.id, 'Ничего не делали, так как не победитель')
