from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton


def notify_about_anniversary_user(user_id: int):
    keyboard = InlineKeyboardMarkup()
    keyboard.add(InlineKeyboardButton('Поздравить', callback_data=f'congratulate:{user_id}'),
                 InlineKeyboardButton('Отклонить', callback_data=f'anniversary_reject:{user_id}')
                 )
    return keyboard


def congratulate_or_not(user_id: int):
    keyboard = InlineKeyboardMarkup()
    keyboard.add(
        InlineKeyboardButton('Поздравить', callback_data=f'admin_congrats:{user_id}'),
        InlineKeyboardButton('Отклонить', callback_data=f'reject:{user_id}'),
                 )
    return keyboard
