from telebot.types import Message, CallbackQuery
from loader import bot
import database.commands as usersbase
from keyboards.inline import admin_keyboard
from config_data.config import ADMIN_IDS


@bot.message_handler(commands=['adminshow'])
def bot_admin_show(message: Message) -> None:
    admin_info = usersbase.select_from_groups()
    bot.send_message(chat_id=message.chat.id, text="Текущие настройки админки в формате\n"
                                                   "id Группа модераторов - id Группа пользователей")
    for i_info in admin_info:
        bot_text = f"{i_info[0][0]}: "
        for j_info in i_info:
            bot_text = f"{bot_text}{j_info[1]} "
        bot.send_message(chat_id=message.chat.id, text=bot_text)


@bot.message_handler(commands=['adminsetup'])
def bot_admin_setup(message: Message):
    if str(message.from_user.id) in ADMIN_IDS:
        # bot.send_message(message.chat.id, text="Я очистил все данные таблицы ")
        # usersbase.truncate_groups()
        msg = bot.send_message(chat_id=message.chat.id,
                               text="Добро пожаловать в меню админки. Добавьте группы пользователей (ГП), из которых "
                                    "будут приходить уведомления в выбранную группу модераторов (ГМ). Формат ввода:\n"
                                    "{id_ГМ} {id_ГП_1} {id_ГП_2} {id_ГП_3}")
        bot.register_next_step_handler(message=msg, callback=process_admin_input)


def process_admin_input(message: Message) -> None:
    groups = message.text.split(" ")

    for i_group in groups:
        if not i_group.isdigit():
            msg = bot.send_message(chat_id=message.chat.id,
                                   text="Ошибка: id_группы - это натуральное число. Попробуйте еще раз. Формат ввода:\n"
                                        "{id_ГМ} {id_ГП_1} {id_ГП_2} {id_ГП_3}")
            bot.register_next_step_handler(message=msg, callback=process_admin_input)
            return

    for i in range(len(groups) - 1):
        usersbase.insert_to_groups(moderator_id=int(groups[0]), group_id=int(groups[i + 1]))

    bot.send_message(chat_id=message.chat.id,
                     text="Сделано! Хотите добавить еще группы?",
                     reply_markup=admin_keyboard.yes_no_keyboard())


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
                          text="Формат ввода:\n {id_ГМ} {id_ГП_1} {id_ГП_2} {id_ГП_3}")
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
