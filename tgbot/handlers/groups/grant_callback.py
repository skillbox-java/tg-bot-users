from contextlib import suppress

from aiogram import Dispatcher, types
from aiogram.dispatcher.filters import Text
from aiogram.dispatcher.handler import CancelHandler
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeEdited, MessageToEditNotFound, MessageNotModified

from tgbot.misc.grant_text import get_great_text
from tgbot.Utils.DBWorker import get_message_in_queue, get_queue, delete_from_queue, vacuum, set_data_granted, \
    count_from_queue


async def grant_user(call: types.CallbackQuery):
    uid = call.data.split('|')[1]
    message_btn = await get_message_in_queue(uid)
    if not message_btn:
        raise CancelHandler()
    message_id = message_btn[0][1]
    group_id_users = message_btn[0][2]
    group_id_mod = message_btn[0][4]
    user_id = message_btn[0][5]
    user = message_btn[0][6]
    count = message_btn[0][7]
    datetime_update = message_btn[0][8]
    datetime_granted = call.message.date
    moder_id = call.from_user.id
    count_for_grant = await count_from_queue(group_id_users)
    username = message_btn[0][10]

    chat_member = await call.bot.get_chat_member(group_id_users, user_id)
    if not chat_member:
        await call.message.delete_reply_markup()
        await call.answer(text="Такого пользователя уже нет в группе", show_alert=True)
        raise CancelHandler()

    text = get_great_text(user, count_for_grant[0][0])

    grant_message = await call.bot.send_message(chat_id=group_id_users, text=text)

    await call.answer()
    await call.message.answer(text=f"Пользователь {user} в группе {grant_message.chat.title} поздравлен")

    with suppress(MessageCantBeEdited, MessageToEditNotFound, MessageNotModified):
        await call.message.delete_reply_markup()

    granted = [(group_id_users, grant_message.chat.title, user_id, user, group_id_mod, moder_id, count, datetime_update,
                datetime_granted, username)]

    queue = await get_queue(group_id=group_id_users, message_id=message_id)
    if queue:
        for message in queue:
            with suppress(MessageCantBeEdited, MessageToEditNotFound, MessageNotModified):
                await call.bot.edit_message_reply_markup(chat_id=message[4], message_id=message[1], reply_markup=None)
            granted.append((message[2], message[3], message[5], message[6], message[4], '', message[7], message[8],
                            '', message[10]))

    await delete_from_queue(group_id_users)
    await vacuum()

    await set_data_granted(values=granted)


def register_grant(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_callback_query_handler(grant_user, Text(startswith='grant'), chat_type=chat_types)
