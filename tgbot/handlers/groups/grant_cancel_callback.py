from aiogram import Dispatcher, types
from aiogram.dispatcher.filters import Text
from aiogram.types import ChatType

from tgbot.Utils.DBWorker import get_message_in_queue


async def cancel_grant_user(call: types.CallbackQuery):
    uid = call.data.split('|')[1]
    message = await get_message_in_queue(uid)
    await call.answer()
    if message:
        await call.bot.edit_message_reply_markup(chat_id=call.message.chat.id, message_id=message[0][1],
                                                 reply_markup=None)


def register_cancel_grant(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP, ChatType.CHANNEL]
    dp.register_callback_query_handler(cancel_grant_user, Text(startswith='can'), chat_type=chat_types,)
