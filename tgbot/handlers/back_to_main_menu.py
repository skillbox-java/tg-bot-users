from contextlib import suppress
from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeEdited
from keyboards.inline import get_main_menu_kb


async def back_to_main(call: types.CallbackQuery, state: FSMContext):
    with suppress(MessageCantBeEdited):
        await call.message.edit_text(text='⚙    ГЛАВНОЕ МЕНЮ    ⚙', reply_markup=await get_main_menu_kb())
    await state.finish()


def register_back_to_main(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(back_to_main,
                                       chat_type=chat_types,
                                       text='back_to_main',
                                       state="*",
                                       is_admin=True)
