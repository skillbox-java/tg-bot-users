from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton


def congratulate_keyboard() -> InlineKeyboardMarkup:
    """
    Функция, которая генерирует инлайн-клавиатуру "Поздравить/Отклонить" для поздравления пользователей из
    команды /unceleb.
    :return InlineKeyboardMarkup:
    """

    keyboard = InlineKeyboardMarkup(row_width=1)
    congr = InlineKeyboardButton(text='Поздравить', callback_data='congr')
    uncongr = InlineKeyboardButton(text='Отклонить', callback_data='uncongr')
    keyboard.add(congr, uncongr)
    return keyboard
