from loader import bot
from database.commands import insert_to_users
import datetime
from database.commands import winner_check, select_id_from_users,\
    temp_save, buttons_remover, storage_cleaner, storage_cleaner_lite, is_winner_id_select, \
    is_winner_record, other_lucky_check, data_finder
from telebot.types import Message, CallbackQuery
from keyboards.inline import new_user_keyboard
from config_data.config import HAPPY_NUMBER


@bot.message_handler(content_types=['new_chat_members'])
def handler_new_member(message: Message) -> None:
    """"
    Хендлер, который обрабатывает вступление в группу пользователей нового пользователя.
    :param Message message: сообщение о вступлении в группу нового пользователя
    :return: None
    """
    count = bot.get_chat_members_count(message.chat.id)
    user_id = message.from_user.id
    chat_name = message.chat.title
    nickname = message.from_user.username
    user_name = message.from_user.first_name
    dtime = datetime.datetime.now().replace(microsecond=0, tzinfo=None)
    moder_id = 258281993 #Я не знаю как лучше модерский айди написать и как он будет передаваться, по этому пока так

    # проверка на то, что новый пользователь не является ботом, еще не был победителем,
    # а также имеет юбилейный номер вступления
    if (not message.from_user.is_bot) and (not winner_check(user_id=user_id)) and \
            (count % HAPPY_NUMBER == 0 or count % HAPPY_NUMBER == 1 or count % HAPPY_NUMBER == 2):

        # если все условия выполнены, записываем пользователя в базу
        insert_to_users(nickname=nickname, user_name=user_name, congr_number=count, chat_name=chat_name,
                        user_id=user_id, dtime_connection=dtime, chat_id=message.chat.id, is_winner=0)


        if not other_lucky_check(count_users=502, chat_id=message.chat.id): #здесь count_users поменяем на count, когда будут реальные условия.
                                                                            # Пока для тестов вписано вручную


            bot_message = bot.send_message(moder_id,
                    f'В {chat_name} вступил юбилейный пользователь {nickname} {user_name}\n'
                    f'Порядковый номер вступления: {count}, время вступления: {dtime}',
                             reply_markup=new_user_keyboard.congratulate_keyboard())

            temp_save(chat_id=moder_id,
                      record_id=select_id_from_users(user_id=message.from_user.id),
                      bot_message_id=bot_message.id, users_chat=message.chat.id)


    else:
        bot.send_message(message.chat.id, f'Что-то пошло не так') #В активной версии нужно убрать, пока оставлено для проверки


@bot.callback_query_handler(func=lambda call: call.data == "grac" or call.data == "decline")
def callback(call: CallbackQuery):
    if call.message:
        winner = is_winner_id_select(bot_message_id=call.message.message_id)
        name = data_finder(bot_message_id=call.message.message_id)[0][0]
        congr_number = data_finder(bot_message_id=call.message.message_id)[0][1]
        users_chat = data_finder(bot_message_id=call.message.message_id)[0][2]
        moders_chat = call.message.chat.id


        if call.data == 'grac':
            is_winner_record(winner_id=winner)
            remove_list = buttons_remover(chat_id=users_chat)

            for message in remove_list:
                bot.delete_message(chat_id=moders_chat, message_id=message)

            storage_cleaner(chat_id=users_chat)

            bot.send_message(users_chat, f'Поздравляю, {name}, '
                                        f'как же удачно попали в нужное время. Вы  '
                                        f'участник {congr_number} коммьюнити. Вас ждут плюшки и печенюшки.')

            bot.send_message(moders_chat, f'Участник {name} поздравлен.')


        else:
            bot.delete_message(chat_id=moders_chat, message_id=call.message.message_id)
            storage_cleaner_lite(message_id=call.message.message_id)

            bot.send_message(moders_chat, f'Участника {name} поздравлять не стали, потому что вы нажали кнопку "не поздравлять"')
