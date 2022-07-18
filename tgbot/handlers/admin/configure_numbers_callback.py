from contextlib import suppress

from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeEdited

from tgbot.keyboards.inline import get_conf_numbers_kb


async def configure_numbers(call: types.CallbackQuery):
    with suppress(MessageCantBeEdited):
        await call.message.edit_text(text='⚙ Настройка таблицы с поздр. номерами ⚙',
                                     reply_markup=get_conf_numbers_kb())


def register_configure_numbers(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(configure_numbers,
                                       chat_type=chat_types,
                                       text='configure_numbers',
                                       is_admin=True
                                       )
