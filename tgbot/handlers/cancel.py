from contextlib import suppress
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted



async def cancel_menu(call: types.CallbackQuery):
    with suppress(MessageCantBeDeleted):
        await call.message.delete()


def register_cancel_menu(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(cancel_menu,
                                       chat_type=chat_types,
                                       text='cancel',
                                       is_admin=True)
