from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton


def yes_no_add_keyboard() -> InlineKeyboardMarkup:
    """
    Функция, которая генерирует инлайн-клавиатуру "да-нет" для меню админки.
    :return InlineKeyboardMarkup:
    """

    keyboard = InlineKeyboardMarkup()
    keyboard.add(InlineKeyboardButton(text="да", callback_data="yes_admin_add"),
                 InlineKeyboardButton(text="нет", callback_data="no_admin_add"))
    return keyboard


def yes_no_proceed_keyboard() -> InlineKeyboardMarkup:
    """
    Функция, которая запрашивает подтверждение, что администратор действитель хочет поменять настройки админки.
    :return InlineKeyboardMarkup:
    """

    keyboard = InlineKeyboardMarkup()
    keyboard.add(InlineKeyboardButton(text="да", callback_data="yes_admin_proceed"),
                 InlineKeyboardButton(text="нет", callback_data="no_admin_proceed"))
    return keyboard
