from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType

from tgbot.Utils.check_number import check_number_in_message

from tgbot.misc.states import Configure


async def get_mod_group(message: types.Message, state: FSMContext):
    if message.text == '/reset':
        await state.finish()
        return
    group_id = await check_number_in_message(message.text)
    if not group_id:
        await message.answer('Введите id группы модераторов, целое число (/reset для сброса)')
        return

    await state.update_data(id_mod=group_id)
    await message.answer('Введите IDs групп пользователей, несколько групп,'
                         ' необходимо разделить через запятую (/reset для сброса)')
    await Configure.AddUserGroups.set()


def register_get_mod_group(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(get_mod_group,
                                chat_type=chat_types,
                                state=Configure.AddModGroups,
                                is_admin=True)
