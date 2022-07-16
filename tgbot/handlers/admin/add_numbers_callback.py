from contextlib import suppress
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted
from misc.states import Configure


async def add_numbers(call: types.CallbackQuery):
    with suppress(MessageCantBeDeleted):
        await call.bot.delete_message(message_id=call.message.message_id, chat_id=call.message.chat.id)

    await call.message.answer(text='Введите id группы, целое число (/reset для сброса)')
    await Configure.AddNumbersGroup.set()


def register_add_numbers(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(add_numbers,
                                       chat_type=chat_types,
                                       text='add_numbers',
                                       is_admin=True)
