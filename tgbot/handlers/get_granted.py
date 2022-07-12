from typing import List
from aiogram import Dispatcher, types
from aiogram.types import ChatType
from tgbot.Utils.DBWorker import get_data_granted


async def get_granted(message: types.Message, ids: List[tuple[str]]):
    granted_list = await get_data_granted(message.chat.id)
    if granted_list:
        for granted in granted_list:
            emoji = '🎉'
            if granted[6]:
                emoji = '👑👑👑'
            await message.answer(text=f'{emoji} “{granted[2]}” 👤 {granted[4]} ({granted[10]}),\n'
                                      f'🔢 {granted[7]} 🕐 {granted[8]}')
    else:
        await message.answer('В группах для модерированния еще нет поздравленных пользователей')


def register_get_granted(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_message_handler(get_granted,
                                chat_type=chat_types,
                                commands=['списокЮбилейный'],
                                is_moder_group=True)
