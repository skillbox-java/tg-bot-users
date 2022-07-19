from telebot.types import Message, CallbackQuery
from loader import bot
from database.commands import get_moderator_id, select_from_groups, insert_to_groups, delete_from_groups
from keyboards.inline import admin_keyboard
from config_data.config import ADMIN_IDS


@bot.message_handler(commands=['adminshow'])
def bot_admin_show(message: Message) -> None:
    """
    Функция, которая выводит текущие настройки админки с группировкой по id группы модератора. Команда доступна только
    в чатах модераторов, которые настроены командой /adminsetup.
    :param Message message: /adminshow
    :return: None
    """
    moderator_ids = get_moderator_id()

    if (message.chat.id in moderator_ids) or (str(message.from_user.id) in ADMIN_IDS):
        admin_info = select_from_groups()

        if admin_info:
            bot.send_message(chat_id=message.chat.id, text="Текущие настройки админки в формате\n"
                                                           "id Группа модераторов: id Группа пользователей")
            for i_info in admin_info:
                bot_text = f"{i_info[0][0]}: "
                for j_info in i_info:
                    bot_text = f"{bot_text}{j_info[1]} "
                bot.send_message(chat_id=message.chat.id, text=bot_text)
        else:
            bot.send_message(chat_id=message.chat.id,
                             text="Настройки еще не выставлены.")


@bot.message_handler(commands=['adminsetup'])
def bot_admin_setup(message: Message) -> None:
    """
    Функция, которая запрашивает подтверждение смены текущих настроек админки. Доступна только пользователям с правами
     администрирования.
    :param Message message: /adminsetup
    :return: None
    """
    moderator_ids = get_moderator_id()

    if str(message.from_user.id) in ADMIN_IDS:
        bot.send_message(chat_id=message.chat.id,
                         text="Пожалуйста, подтвердите, что вы действительно хотите поменять настройки админки. "
                              "Это приведет к полному удалению текущих настроек и отменить это действие будет "
                              "невозможно!",
                         reply_markup=admin_keyboard.yes_no_proceed_keyboard())
    elif message.chat.id in moderator_ids:
        bot.send_message(chat_id=message.chat.id,
                         text=f"Ошибка доступа: эта команда доступна только пользователям с правами "
                              f"администрирования. Доступ есть у пользователей с такими user_id:\n{ADMIN_IDS}")


def process_admin_input(message: Message) -> None:
    """
    Функция, которая сохраняет введенные администратором id групп модераторов и пользователей.
    :param Message message: id групп модератора и его пользователей через пробелы
    :return: None
    """
    groups = message.text.split(" ")

    for i_group in groups:
        if not (i_group.startswith("-") and i_group[1:].isdigit()):
            msg = bot.send_message(chat_id=message.chat.id,
                                   text="Ошибка: id_группы - это отрицательное число. Попробуйте еще раз. Формат"
                                        " ввода:\n id_ГМ id_ГП_1 id_ГП_2 id_ГП_3")
            bot.register_next_step_handler(message=msg, callback=process_admin_input)
            return

    for i in range(len(groups) - 1):
        insert_to_groups(moderator_id=int(groups[0]), group_id=int(groups[i + 1]))

    bot.send_message(chat_id=message.chat.id,
                     text="Сделано! Хотите добавить еще группы?",
                     reply_markup=admin_keyboard.yes_no_add_keyboard())


@bot.callback_query_handler(func=lambda call: call.data == "yes_admin_proceed")
def callback_query(call: CallbackQuery) -> None:
    """
    Колбек, обрабатывайщий подтверждение, что администратор действитель хочет поменять настройки админки. Сбрасывает
    текущие настройки и перенаправляет на обработчик process_admin_input.
    :param CallbackQuery call: yes_admin_proceed
    :return: None
    """
    delete_from_groups()
    msg = bot.edit_message_text(chat_id=call.message.chat.id,
                                message_id=call.message.message_id,
                                text="Добро пожаловать в меню админки. Добавьте группы пользователей (ГП), из которых "
                                     "будут приходить уведомления в выбранную группу модераторов (ГМ). Формат ввода:\n"
                                     "id_ГМ id_ГП_1 id_ГП_2 id_ГП_3")
    bot.register_next_step_handler(msg, process_admin_input)


@bot.callback_query_handler(func=lambda call: call.data == "no_admin_proceed")
def callback_query(call: CallbackQuery) -> None:
    """
    Колбек, обрабатывайщий отказ администратора поменять настройки админки.
    :param CallbackQuery call: no_admin_proceed
    :return: None
    """
    bot.edit_message_text(chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          text="Вы отказались менять настройки админки.")


@bot.callback_query_handler(func=lambda call: call.data == "yes_admin_add")
def callback_query(call: CallbackQuery) -> None:
    """
    Колбек, обрабатывайщий необходимость ввести еще группы в меню админки. Перенаправляет на обработчик
    process_admin_input.
    :param CallbackQuery call: yes_admin_add
    :return: None
    """
    msg = bot.edit_message_text(chat_id=call.message.chat.id,
                                message_id=call.message.message_id,
                                text="Формат ввода:\n id_ГМ id_ГП_1 id_ГП_2 id_ГП_3")
    bot.register_next_step_handler(msg, process_admin_input)


@bot.callback_query_handler(func=lambda call: call.data == "no_admin_add")
def callback_query(call: CallbackQuery) -> None:
    """
    Колбек, обрабатывайщий окончание ввода групп в меню админки.
    :param CallbackQuery call: no_admin_add
    :return: None
    """
    bot.edit_message_text(chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          text="Сделано! Чтобы посмотреть все текущие настройки админки введите команду /adminshow")
