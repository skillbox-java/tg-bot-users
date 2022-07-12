from aiogram.types import InlineKeyboardMarkup, InlineKeyboardButton

async def get_gran_kb(uid):
    grant_btn = InlineKeyboardButton('Поздравить!', callback_data=f'grant|{uid}')
    cancel_btn = InlineKeyboardButton('Отмена', callback_data=f'can|{uid}')
    grant_kb = InlineKeyboardMarkup().add(grant_btn, cancel_btn)
    return grant_kb
