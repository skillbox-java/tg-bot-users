from contextlib import suppress
from aiogram import Dispatcher
from aiogram.types import CallbackQuery, ChatType
from aiogram.utils.exceptions import MessageToEditNotFound, MessageCantBeDeleted
from tgbot.keyboards.inline import cb


async def show_granted_cb(call: CallbackQuery, callback_data: dict) -> None:
    """
    Функция коллбека для закрытия меню для /списокЮбилейный
    :param call: CallbackQuery
    :param callback_data: dict
    :return: None
    """
    with suppress(MessageCantBeDeleted, MessageToEditNotFound):
        await call.message.delete()


def register_show_granted_cb(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_callback_query_handler(show_granted_cb, cb.filter(ids=0), chat_type=chat_types)
