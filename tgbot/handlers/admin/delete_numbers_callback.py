from contextlib import suppress

from aiogram import md
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted

from tgbot.misc.states import Configure


async def delete_numbers(call: types.CallbackQuery):
    with suppress(MessageCantBeDeleted):
        await call.message.delete()

    await call.message.answer('Введите IDs строк для удаления записей из базы, целые числа, '
                              'если нужно удалить несколько, вводите через запятую (/reset для сброса)')
    await Configure.DeleteNumbers.set()


def register_delete_numbers_cb(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(delete_numbers,
                                       chat_type=chat_types,
                                       text='delete_numbers',
                                       is_admin=True)
