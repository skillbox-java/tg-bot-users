from aiogram import Dispatcher, types
from aiogram.dispatcher.filters import Text
from aiogram.types import ChatType

from tgbot.Utils.DBWorker import get_message_in_queue


async def cancel_grant_user(call: types.CallbackQuery) -> None:
    """
    Функция коллбека, убирает кнопку поздравления пользователя
    :param call: types.CallbackQuery
    :return: None
    """
    uid = call.data.split('|')[1]
    message = await get_message_in_queue(uid)
    await call.answer()
    if message:
        await call.message.delete_reply_markup()


def register_cancel_grant(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_callback_query_handler(cancel_grant_user, Text(startswith='can'), chat_type=chat_types,)
