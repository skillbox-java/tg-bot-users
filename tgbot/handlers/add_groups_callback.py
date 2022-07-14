from contextlib import suppress
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted
from misc.states import Configure


async def add_groups(call: types.CallbackQuery):
    with suppress(MessageCantBeDeleted):
        await call.bot.delete_message(message_id=call.message.message_id, chat_id=call.message.chat.id)

    await call.message.answer(text='Введите id группы модераторов, целое число (/reset для сброса)')
    await Configure.AddModGroups.set()


def register_add_groups(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(add_groups,
                                       chat_type=chat_types,
                                       text='add_groups',
                                       state="*",
                                       is_admin=True)
