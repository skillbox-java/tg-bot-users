from contextlib import suppress

from aiogram import md
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted

from tgbot.Utils.DBWorker import get_numbers


async def show_numbers(call: types.CallbackQuery):
    groups = await get_numbers()
    text = md.hbold('ID |ID Группы|Номера для поздравления\n\n')

    with suppress(MessageCantBeDeleted):
        await call.message.delete()

    if groups:
        for group in groups:
            text += md.hunderline(f'{group[0]} | {group[1]} | {group[2]} |\n')
        await call.message.answer(text=text)
    else:
        await call.message.answer(text='Таблица еще пуста')


def register_show_numbers(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(show_numbers,
                                       chat_type=chat_types,
                                       text='show_numbers',
                                       state="*",
                                       is_admin=True)