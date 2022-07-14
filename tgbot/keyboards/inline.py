from aiogram.types import InlineKeyboardMarkup, InlineKeyboardButton

async def get_gran_kb(uid):
    grant_btn = InlineKeyboardButton('Поздравить!', callback_data=f'grant|{uid}')
    cancel_btn = InlineKeyboardButton('Отмена', callback_data=f'can|{uid}')
    grant_kb = InlineKeyboardMarkup().add(grant_btn, cancel_btn)
    return grant_kb


async def get_conf_groups_kb():
    show_btn = InlineKeyboardButton('Показать таблицу', callback_data='show_groups')
    add_btn = InlineKeyboardButton('Добавить строку', callback_data='add_groups')
    delete_btn = InlineKeyboardButton('Удалить строку', callback_data='delete_groups')
    reset_btn = InlineKeyboardButton('Отмена', callback_data='reset')
    conf_groups_kb = InlineKeyboardMarkup(row_width=1).add(show_btn, add_btn, delete_btn, reset_btn)
    return conf_groups_kb
