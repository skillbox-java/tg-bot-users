from contextlib import suppress
from typing import List

from aiogram.utils.exceptions import MessageToDeleteNotFound, MessageCantBeDeleted

from tgbot.keyboards.inline import get_gran_kb
from aiogram import Dispatcher, types
from aiogram.types import ChatType

from tgbot.Utils.DBWorker import get_queue, update_data_queue


async def restore(message: types.Message, ids: List[tuple]) -> None:
    """
    Функция для повторной отправки сообщений с кнопками для поздр. в группу модераторов
    :param message: types.Message
    :param ids: List[tuple]
    :return: None
    """
    users_groups_ids = ids[0][0].split(',')

    for group_id in users_groups_ids:
        messages_in_queue = await get_queue(int(group_id))
        if messages_in_queue:
            for message_in_queue in messages_in_queue:
                with suppress(MessageToDeleteNotFound, MessageCantBeDeleted):
                    await message.bot.delete_message(message.chat.id, message_in_queue[1])
                text = f'🎉 “{message_in_queue[3]}” 👤 {message_in_queue[6]} ({message_in_queue[10]}),\n' \
                       f'🔢 {message_in_queue[7]} 🕐 {message_in_queue[8]}'
                new_message = await message.bot.send_message(message.chat.id, text=text,
                                                             reply_markup=get_gran_kb(uid=message_in_queue[9]))
                await update_data_queue(message_id=new_message.message_id, old_message_id=message_in_queue[1],
                                        group_id=message_in_queue[2])
        else:
            await message.answer('Нет сообщений для восстановления')


def register_restore(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_message_handler(restore,
                                chat_type=chat_types,
                                commands=['восстановить'],
                                is_moder_group=True)
