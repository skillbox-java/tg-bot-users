from typing import List
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from tgbot.Utils.DBWorker import get_queue


async def check_queue(message: types.Message, ids: List[tuple[str]]):
    users_groups_ids = ids[0][0].split(',')

    for group_id in users_groups_ids:
        messages_in_queue = await get_queue(group_id)
        if messages_in_queue:
            text = f'Есть не поздравленные пользователи в группе {messages_in_queue[0][3]}:\n'
            for message_in_queue in messages_in_queue:
                text += f'- Пользователь {message_in_queue[6]} номер вступления {message_in_queue[7]}\n'
            await message.answer(text=text)
        else:
            await message.answer('Нет не поздравленных пользователей')


def register_check_queue(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_message_handler(check_queue,
                                chat_type=chat_types,
                                commands=['проверка'],
                                is_moder_group=True)
