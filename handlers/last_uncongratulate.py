from telebot.types import Message, CallbackQuery
from loader import bot
from database.commands import temp_cleaner, select_last_uncongratulate, get_all_moderator_id, \
    temp_save_unceleb, get_chat_id_unceleb, is_winner_id_select_unceleb, is_winner_record, buttons_remover_unceleb, \
    storage_cleaner_unceleb, record_cleaner_unceleb, data_finder, get_group_id, select_last_congr_number_from_users,\
    is_uncongr
import datetime
from keyboards.inline import unceleb_keyboard
from config_data.config import HAPPY_NUMBER


@bot.message_handler(commands=['unceleb'])
def bot_uncongratulate(message: Message) -> None:
    """"
    Хендлер, который обрабатывает команду /unceleb по выводу последних непоздравленных пользователей.
    :param Message message: /unceleb
    :return: None
    """
    moderator_ids = get_all_moderator_id()

    if message.chat.id in moderator_ids:
        # чистим таблицу temp_unceleb
        temp_cleaner()

        # получаем id всех групп пользователей для этой группы модераторов
        group_id_list = get_group_id(moderator_id=message.chat.id)

        # получаем список всех последних непоздравленных пользователей
        uncelebs_list = []

        # для каждой группы пользователей находим номер последнего зарегистрированного пользователя
        for group_id in group_id_list:
            last_congr_number = select_last_congr_number_from_users(chat_id=group_id)

            # если в группе зарегистрированы пользователи, проверяем есть ли победитель для последнего юбилейного номера
            if last_congr_number:
                if is_uncongr(congr_number=last_congr_number, chat_id=group_id):

                    # если победителя нет, то мы добавляем всех последних непоздравленных пользователей для этой
                    # группы в общий список последних непоздравленных пользователей
                    last_uncong_users = select_last_uncongratulate(congr_number=last_congr_number, chat_id=group_id)
                    for user in last_uncong_users:
                        uncelebs_list.append(user)

        # выводим информацию о последних непоздравленных пользователях, предлагаем их поздравить и
        # сохраняем данные в таблицу temp_unceleb
        for unceleb in uncelebs_list:
            dtime = datetime.datetime.strptime(unceleb[5], '%Y-%m-%d %H:%M:%S.%f').strftime('%d.%m.%y %H:%M')

            bot_message = bot.send_message(chat_id=message.chat.id,
                                           text=f'\U0001F389  "{unceleb[4]}"  \U0001F464  {unceleb[3]} '
                                                f'(@{unceleb[2]})\n\U0001F522  {unceleb[1]}  \U0001F550 	{dtime}',
                                           reply_markup=unceleb_keyboard.congratulate_keyboard())

            temp_save_unceleb(chat_id=unceleb[0],
                              record_id=unceleb[6],
                              bot_message_id=bot_message.id)


@bot.callback_query_handler(func=lambda call: call.data == "congr" or call.data == "uncongr")
def callback(call: CallbackQuery) -> None:
    """
    Колбек, обрабатывайщий нажатие кнопки Поздравить или Отклонить в команде /unceleb.
    :param CallbackQuery call: congr or uncongr
    :return: None
    """
    name = data_finder(bot_message_id=call.message.message_id)[0]
    congr_number = data_finder(bot_message_id=call.message.message_id)[1]
    users_chat = data_finder(bot_message_id=call.message.message_id)[2]
    moders_chat = call.message.chat.id

    if call.data == 'congr':
        winner_chat_id = get_chat_id_unceleb(bot_message_id=call.message.message_id)
        winner_id = is_winner_id_select_unceleb(bot_message_id=call.message.message_id)

        # записываем, что пользователь выиграл
        is_winner_record(winner_id=winner_id)

        # удаляем все остальные сообщения бота и чистим информацию о сообщениях в базе данных таблицы temp_storage
        remove_list = buttons_remover_unceleb(chat_id=winner_chat_id)
        for message in remove_list:
            bot.delete_message(chat_id=call.message.chat.id, message_id=message)
        storage_cleaner_unceleb(chat_id=call.message.chat.id)

        # поздравляем победителя в группе пользователей
        bot.send_message(chat_id=users_chat,
                         text=f'\U0001F389 Поздравляю, {name}, как же удачно попали в нужное время!\n'
                              f'Вы участник {congr_number - congr_number % HAPPY_NUMBER} '
                              f'коммьюнити.\nВас ждут плюшки и печенюшки! \U0001F389')

        # уведомляем группу модераторов о поздравлении победителя
        bot.send_message(chat_id=moders_chat,
                         text=f'Участник {name} поздравлен! \U0001F389')

    else:
        # удаляем сообщение бота и чистим информацию о сообщении в базе данных таблицы temp_unceleb
        bot.delete_message(chat_id=call.message.chat.id, message_id=call.message.message_id)
        record_cleaner_unceleb(bot_message_id=call.message.message_id)

        # уведомляем группу модераторов о том, что участника не поздравили
        bot.send_message(moders_chat, f'Участника {name} не поздравили.')
