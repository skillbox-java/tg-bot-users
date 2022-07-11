from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton


def congratulate_user(user_id: int):
    keyboard = InlineKeyboardMarkup()
    keyboard.add(InlineKeyboardButton('Поздравить', callback_data=f'congratulate:{user_id}'),)
    return keyboard


def congratulate_userv2(user_id: int):
    keyboard = InlineKeyboardMarkup()
    keyboard.add(InlineKeyboardButton('Поздравить', callback_data=f'anniversary_list:{user_id}'),)
    return keyboard
