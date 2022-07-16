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
    back_to_main_btn = InlineKeyboardButton('Главное меню', callback_data='back_to_main')
    conf_groups_kb = InlineKeyboardMarkup(row_width=1).add(show_btn, add_btn, delete_btn, back_to_main_btn)
    return conf_groups_kb


async def get_main_menu_kb():
    config_groups_btn = InlineKeyboardButton('Настройка таблицы групп', callback_data='configure_groups')
    config_numbers_btn = InlineKeyboardButton('Настройка таблицы номеров', callback_data='configure_numbers')
    cancel_btn = InlineKeyboardButton('Отмена', callback_data='cancel')
    main_kb = InlineKeyboardMarkup(row_width=1).add(config_groups_btn, config_numbers_btn, cancel_btn)
    return main_kb


async def get_conf_numbers_kb():
    show_btn = InlineKeyboardButton('Показать таблицу', callback_data='show_numbers')
    add_btn = InlineKeyboardButton('Добавить строку', callback_data='add_numbers')
    delete_btn = InlineKeyboardButton('Удалить строку', callback_data='delete_numbers')
    back_to_main_btn = InlineKeyboardButton('Главное меню', callback_data='back_to_main')
    conf_numbers_kb = InlineKeyboardMarkup(row_width=1).add(show_btn, add_btn, delete_btn, back_to_main_btn)
    return conf_numbers_kb
