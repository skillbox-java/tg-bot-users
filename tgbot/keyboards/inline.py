from aiogram import types
from aiogram.types import InlineKeyboardMarkup, InlineKeyboardButton
from aiogram.utils.callback_data import CallbackData


def get_gran_kb(uid: str) -> InlineKeyboardMarkup:
    """
    Возвращает inline клав. для поздравления
    :param uid:
    :return: InlineKeyboardMarkup
    """
    grant_btn = InlineKeyboardButton('Поздравить!', callback_data=f'grant|{uid}')
    cancel_btn = InlineKeyboardButton('Отмена', callback_data=f'can|{uid}')
    grant_kb = InlineKeyboardMarkup().add(grant_btn, cancel_btn)
    return grant_kb


def get_conf_groups_kb() -> InlineKeyboardMarkup:
    """
    Возвращает inline клав. для настройки таблицы соотв. групп
    :return: InlineKeyboardMarkup
    """
    show_btn = InlineKeyboardButton('Показать таблицу', callback_data='show_groups')
    add_btn = InlineKeyboardButton('Добавить строку', callback_data='add_groups')
    delete_btn = InlineKeyboardButton('Удалить строку', callback_data='delete_groups')
    back_to_main_btn = InlineKeyboardButton('Главное меню', callback_data='back_to_main')
    conf_groups_kb = InlineKeyboardMarkup(row_width=1).add(show_btn, add_btn, delete_btn, back_to_main_btn)
    return conf_groups_kb


def get_main_menu_kb() -> InlineKeyboardMarkup:
    """
    Возвращает inline клав. для главного меню
    :return: InlineKeyboardMarkup
    """
    config_groups_btn = InlineKeyboardButton('Настройка таблицы групп', callback_data='configure_groups')
    config_numbers_btn = InlineKeyboardButton('Настройка таблицы номеров', callback_data='configure_numbers')
    cancel_btn = InlineKeyboardButton('Отмена', callback_data='cancel')
    main_kb = InlineKeyboardMarkup(row_width=1).add(config_groups_btn, config_numbers_btn, cancel_btn)
    return main_kb


def get_conf_numbers_kb():
    """
    Возвращает inline клав. для настройки таблицы c поздр. номерами
    :return: InlineKeyboardMarkup
    """
    show_btn = InlineKeyboardButton('Показать таблицу', callback_data='show_numbers')
    add_btn = InlineKeyboardButton('Добавить строку', callback_data='add_numbers')
    delete_btn = InlineKeyboardButton('Удалить строку', callback_data='delete_numbers')
    back_to_main_btn = InlineKeyboardButton('Главное меню', callback_data='back_to_main')
    conf_numbers_kb = InlineKeyboardMarkup(row_width=1).add(show_btn, add_btn, delete_btn, back_to_main_btn)
    return conf_numbers_kb


cb = CallbackData('gr', 'ids')


def get_list_granted_kb(user_groups: dict) -> types.InlineKeyboardMarkup:
    """
    Фабрика коллбеков для вывода групп, если их больше одной, при отправке команды /списокЮбилейный
    :param user_groups: dict
    :return: types.InlineKeyboardMarkup
    """
    markup = types.InlineKeyboardMarkup()
    for ids, user_group in user_groups.items():
        markup.add(
            types.InlineKeyboardButton(
                user_group,
                callback_data=cb.new(ids=ids)),
        )
    markup.add(
        types.InlineKeyboardButton(
            'Отмена',
            callback_data=cb.new(ids=0)),
    )
    return markup
