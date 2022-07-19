from contextlib import suppress
from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeEdited
from tgbot.keyboards.inline import get_main_menu_kb


async def back_to_main(call: types.CallbackQuery, state: FSMContext) -> None:
    """
    Функция коллбэка для возрврата в главное меню и сброса состояний
    :param call: types.CallbackQuery
    :param state: FSMContext
    :return: None
    """
    with suppress(MessageCantBeEdited):
        await call.message.edit_text(text='⚙    ГЛАВНОЕ МЕНЮ    ⚙', reply_markup=get_main_menu_kb())
    await state.finish()


def register_back_to_main(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(back_to_main,
                                       chat_type=chat_types,
                                       text='back_to_main',
                                       state="*",
                                       is_admin=True)
