from contextlib import suppress

from aiogram import md
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted

from tgbot.Utils.DBWorker import get_groups


async def show_groups(call: types.CallbackQuery):
    groups = await get_groups()
    text = md.hunderline('ID |   Группа модераторов   |   Группы пользователей\n')

    with suppress(MessageCantBeDeleted):
        await call.message.bot.delete_message(message_id=call.message.message_id, chat_id=call.message.chat.id)

    if groups:
        for group in groups:
            text += md.hbold(f'{group[0]}  | {group[1]} | {group[2]} |\n')
            text += ' ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n'
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