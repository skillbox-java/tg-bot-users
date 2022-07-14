from contextlib import suppress
from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted


async def reset_menu(call: types.CallbackQuery, state: FSMContext):
    with suppress(MessageCantBeDeleted):
        await call.bot.delete_message(message_id=call.message.message_id, chat_id=call.message.chat.id)

    await state.finish()


def register_reset_menu(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(reset_menu,
                                       chat_type=chat_types,
                                       text='reset',
                                       state="*",
                                       is_admin=True)
