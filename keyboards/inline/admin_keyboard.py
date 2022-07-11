from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton


def yes_no_keyboard() -> InlineKeyboardMarkup:
    """
    Функция, которая генерирует инлайн-клавиатуру "да-нет" для меню админки.
    :return InlineKeyboardMarkup:
    """

    keyboard = InlineKeyboardMarkup()
    keyboard.add(InlineKeyboardButton(text="да", callback_data="yes_admin"),
                 InlineKeyboardButton(text="нет", callback_data="no_admin"))
    return keyboard
