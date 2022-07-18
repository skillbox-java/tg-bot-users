from aiogram import Dispatcher, types
from aiogram.types import ChatType
from tgbot.keyboards.inline import get_main_menu_kb


async def main_menu(message: types.Message):
    await message.answer(text='⚙    ГЛАВНОЕ МЕНЮ    ⚙', reply_markup=get_main_menu_kb())


def register_main_menu(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(main_menu,
                                chat_type=chat_types,
                                commands=['configure'],
                                is_admin=True
                                )
