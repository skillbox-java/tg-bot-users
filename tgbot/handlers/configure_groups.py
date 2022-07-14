from typing import List
from aiogram import Dispatcher, types
from aiogram.types import ChatType

from keyboards.inline import get_conf_groups_kb
from tgbot.Utils.DBWorker import get_data_granted


async def configure_groups(message: types.Message):
    await message.answer(text='⚙⚙⚙', reply_markup=await get_conf_groups_kb())



def register_configure_groups(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(configure_groups,
                                chat_type=chat_types,
                                commands=['configure_groups'],
                                is_admin=True
                                )
