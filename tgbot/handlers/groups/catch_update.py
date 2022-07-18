from typing import List
from aiogram.utils.markdown import quote_html
from tgbot.keyboards.inline import get_gran_kb
from aiogram import Dispatcher, types
from aiogram.types import ChatType
import uuid
from tgbot.Utils.get_user_link import get_link
from tgbot.Utils.DBWorker import set_data_queue


async def new_chat(update: types.ChatMemberUpdated, ids: List[tuple[int]], count: int):
    link = await get_link(update.new_chat_member.user)
    uid = str(uuid.uuid4())
    if update.new_chat_member.user.username:
        username = f'@{quote_html(update.new_chat_member.user.username)}'
    else:
        username = 'ника нет'
    text = f'🎉 В “{update.chat.title}” группу вступил юбилейный пользователь\n' \
           f'{link} ({username}),\n' \
           f'🔢{count}. 🕐Время вступления {update.date}'

    message = await update.bot.send_message(ids[0][0], text=text, reply_markup=get_gran_kb(uid=uid))

    await set_data_queue(
        values=(
            message.message_id, update.chat.id, update.chat.title, ids[0][0], update.new_chat_member.user.id, link,
            count, update.date, uid, username))


def register_catch(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_chat_member_handler(new_chat,
                                    chat_type=chat_types,
                                    is_group_join=True,
                                    is_not_granted=True,
                                    is_grant_count=True,
                                    is_user_group=True)
