from telebot.types import Message
from loader import bot
import database.commands as usersbase
import datetime
from telebot import types


@bot.message_handler(commands=['unceleb'])
def bot_uncongratulate(message: Message):
    usersbase.temp_cleaner()
    markup = types.InlineKeyboardMarkup(row_width=1)
    congr = types.InlineKeyboardButton(text='Поздравить', callback_data='congr')
    uncongr = types.InlineKeyboardButton(text='Отклонить', callback_data='uncongr')
    markup.add(congr, uncongr)
    uncelebs = usersbase.select_last_uncongratulate(message.chat.id)
    gr_id = usersbase.select_group_id(message.chat.id)
    temp_id = 0
    uncelebs_list = []
    for id, gr in enumerate(gr_id):
        temp_id += gr_id[id][1]
        # print(uncelebs[temp_id - 1], uncelebs[temp_id - 2], uncelebs[temp_id - 3])

        uncelebs_list.append(uncelebs[temp_id - 1])
        if uncelebs[temp_id - 2][1]//500 == uncelebs[temp_id - 1][1]//500:
            uncelebs_list.append(uncelebs[temp_id - 2])
        if uncelebs[temp_id - 3][1] // 500 == uncelebs[temp_id - 1][1]//500:
            uncelebs_list.append(uncelebs[temp_id - 3])

    for unceleb in uncelebs_list:
        dtime = datetime.datetime.strptime(unceleb[5], '%Y-%m-%d %H:%M:%S.%f').strftime('%d.%m.%y %H:%M')
        bot_message = bot.send_message(chat_id=message.chat.id,
                         text=f'\U0001F389  "{unceleb[4]}"  \U0001F464  {unceleb[3]}'
                              f'(@{unceleb[2]})\n\U0001F522  {unceleb[1]}  \U0001F550 	{dtime}',
                         reply_markup=markup)
        # print(unceleb[6])
        usersbase.temp_save_unceleb(chat_id=unceleb[0],
                  record_id=unceleb[6],
                  bot_message_id=bot_message.id)


@bot.callback_query_handler(func=lambda call: call.data == "congr" or call.data == "uncongr")
def callback(call):
    if call.message:
        if call.data == 'congr':
            winner_chat_id = usersbase.get_chat_id_unceleb(bot_message_id=call.message.message_id)

            winner = usersbase.is_winner_id_select_unceleb(bot_message_id=call.message.message_id)
            # print(winner)
            usersbase.is_winner_record(winner_id=winner)
            remove_list = usersbase.buttons_remover_unceleb(chat_id=winner_chat_id)
            for message in remove_list:
                bot.delete_message(chat_id=call.message.chat.id, message_id=message)
            usersbase.storage_cleaner_unceleb(chat_id=call.message.chat.id)
            celeb_name = call.message.text.split()
            bot.send_message(call.message.chat.id, f'Поздравили {celeb_name[0]} {celeb_name[3]}!!!')
        else:
            bot.delete_message(chat_id=call.message.chat.id, message_id=call.message.message_id)
            usersbase.record_cleaner_unceleb(bot_message_id=call.message.message_id)
            #bot.send_message(call.message.chat.id, 'Ничего не делали, так как не победитель')
