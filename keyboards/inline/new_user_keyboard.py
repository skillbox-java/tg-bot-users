from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton


def congratulate_keyboard() -> InlineKeyboardMarkup:
    """
    Функция, которая генерирует инлайн-клавиатуру "Поздравить/Отклонить" для поздравления юбилейного пользователя.
    :return InlineKeyboardMarkup:
    """

    keyboard = InlineKeyboardMarkup(row_width=1)
    congratulations = InlineKeyboardButton(text='Поздравляем', callback_data='grac')
    shame = InlineKeyboardButton(text='Не поздравляем', callback_data='decline')
    keyboard.add(congratulations, shame)
    return keyboard
