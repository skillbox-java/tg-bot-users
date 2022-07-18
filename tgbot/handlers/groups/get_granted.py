from typing import List
from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType

from tgbot.keyboards.inline import get_list_granted_kb
from tgbot.misc.show_granted import send_granted_message
from tgbot.Utils.DBWorker import get_data_granted, get_users_groups


async def get_granted(message: types.Message, ids: List[tuple[str]], state: FSMContext):
    granted_list = await get_data_granted(message.chat.id)
    if granted_list:
        user_groups_ids = {id_user_gr[1]: id_user_gr[2] for id_user_gr in granted_list}
        if len(user_groups_ids) > 1:
            await message.answer(text='Выберите группу', reply_markup=get_list_granted_kb(user_groups_ids))
        else:
            await send_granted_message(granted_list, message)
    else:
        await message.answer('В группах для модерирования еще нет поздравленных пользователей')


def register_get_granted(dp: Dispatcher):
    chat_types = [ChatType.GROUP, ChatType.SUPERGROUP]
    dp.register_message_handler(get_granted,
                                chat_type=chat_types,
                                commands=['списокЮбилейный'],
                                is_moder_group=True)
