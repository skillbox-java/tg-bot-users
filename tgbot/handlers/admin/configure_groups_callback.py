from contextlib import suppress

from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeEdited

from tgbot.keyboards.inline import get_conf_groups_kb


async def configure_groups(call: types.CallbackQuery) -> None:
    """
    Функция коллбека для вызова меню конфигурирования таблицы соответствия групп
    :param call: types.CallbackQuery
    :return: None
    """
    with suppress(MessageCantBeEdited):
        await call.message.edit_text(text='⚙ Настройка таблицы соответствия групп ⚙',
                                     reply_markup=get_conf_groups_kb())


def register_configure_groups(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(configure_groups,
                                       chat_type=chat_types,
                                       text='configure_groups',
                                       is_admin=True
                                       )
