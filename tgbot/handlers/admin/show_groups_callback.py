from contextlib import suppress

from aiogram import md
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted

from tgbot.Utils.DBWorker import get_groups


async def show_groups(call: types.CallbackQuery) -> None:
    """
    Функция коллбека для показа данных из таблицы соответствия groups
    :param call: types.CallbackQuery
    :return: None
    """
    groups = await get_groups()
    text = md.hbold('| ID | Группа модераторов | Группы пользователей |\n\n')

    with suppress(MessageCantBeDeleted):
        await call.message.delete()

    if groups:
        for group in groups:
            text += md.hunderline(f'| {group[0]} | {group[1]} | {group[2]} |\n')
        await call.message.answer(text=text)
    else:
        await call.message.answer(text='Таблица еще пуста')


def register_show_groups(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(show_groups,
                                       chat_type=chat_types,
                                       text='show_groups',
                                       state="*",
                                       is_admin=True)