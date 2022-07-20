from loader import bot
from database.commands import insert_to_users
import datetime
from database.commands import winner_check, select_id_from_users,\
    temp_save, buttons_remover, storage_cleaner, storage_cleaner_lite, is_winner_id_select, \
    is_winner_record, other_lucky_check, data_finder, get_moderator_id, get_all_group_id
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
    group_ids = get_all_group_id()

    if message.chat.id in group_ids:
        count = bot.get_chat_members_count(message.chat.id)
        user_id = message.from_user.id
        chat_name = message.chat.title
        nickname = message.from_user.username
        user_name = message.from_user.first_name
        dtime = datetime.datetime.now()

        # проверка на то, что новый пользователь не является ботом, еще не был победителем,
        # а также имеет юбилейный номер вступления
        if (not message.from_user.is_bot) and (not winner_check(user_id=user_id)) and \
                (count % HAPPY_NUMBER == 0 or count % HAPPY_NUMBER == 1 or count % HAPPY_NUMBER == 2):

            # если все условия выполнены, записываем пользователя в базу в таблицу users
            insert_to_users(nickname=nickname, user_name=user_name, congr_number=count, chat_name=chat_name,
                            user_id=user_id, dtime_connection=dtime, chat_id=message.chat.id, is_winner=0)

            # проверка, что для юбилейного номера нового пользователя в этом чате еще никого не поздравили
            if not other_lucky_check(count_users=count, chat_id=message.chat.id):

                # если условие выполнено, предлагаем модераторам поздравить пользователя и
                # сохраняем данные в таблицу temp_storage
                moderator_ids = get_moderator_id(group_id=message.chat.id)

                for moderator_id in moderator_ids:
                    bot_message = bot.send_message(chat_id=moderator_id,
                                                   text=f'В {chat_name} вступил юбилейный пользователь {nickname} '
                                                        f'{user_name}\nПорядковый номер вступления: {count}, время '
                                                        f'вступления: {dtime}',
                                                   reply_markup=new_user_keyboard.congratulate_keyboard())

                    temp_save(chat_id=moderator_id,
                              record_id=select_id_from_users(user_id=message.from_user.id),
                              bot_message_id=bot_message.id)


@bot.callback_query_handler(func=lambda call: call.data == "grac" or call.data == "decline")
def callback(call: CallbackQuery) -> None:
    """
    Колбек, обрабатывайщий нажатие кнопки Поздравить или Отклонить в группе модераторов.
    :param CallbackQuery call: grac or decline
    :return: None
    """
    winner_id = is_winner_id_select(bot_message_id=call.message.message_id)
    user_data = data_finder(bot_message_id=call.message.message_id)[0]
    name = user_data[0]
    congr_number = user_data[1]
    users_chat = user_data[2]
    moders_chat = call.message.chat.id

    if call.data == 'grac':
        # записываем, что пользователь выиграл
        is_winner_record(winner_id=winner_id)

        # удаляем все остальные сообщения бота и чистим информацию о сообщениях в базе данных таблицы temp_storage
        remove_list = buttons_remover(chat_id=users_chat)
        for message in remove_list:
            bot.delete_message(chat_id=moders_chat, message_id=message)
        storage_cleaner(chat_id=users_chat)

        # поздравляем победителя в группе пользователей
        bot.send_message(chat_id=users_chat,
                         text=f'\U0001F389 Поздравляю, {name}, как же удачно попали в нужное время!\n'
                              f'Вы участник {congr_number - congr_number % HAPPY_NUMBER} '
                              f'коммьюнити.\nВас ждут плюшки и печенюшки! \U0001F389')

        # уведомляем группу модераторов о поздравлении победителя
        bot.send_message(chat_id=moders_chat,
                         text=f'Участник {name} поздравлен! \U0001F389')

    else:
        # удаляем сообщение бота и чистим информацию о сообщении в базе данных таблицы temp_storage
        bot.delete_message(chat_id=moders_chat, message_id=call.message.message_id)
        storage_cleaner_lite(bot_message_id=call.message.message_id)

        # уведомляем группу модераторов о том, что участника не поздравили
        bot.send_message(moders_chat, f'Участника {name} не поздравили.')
