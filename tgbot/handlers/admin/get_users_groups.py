from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType

from tgbot.Utils.check_message_user_groups import check_users_groups
from tgbot.Utils.delete_doubles import delete_doubles_ids
from tgbot.Utils.get_ids_for_grant_numbers import get_ids_for_multiple_record
from tgbot.keyboards.inline import get_conf_groups_kb
from tgbot.misc.states import Configure
from tgbot.Utils.DBWorker import set_data_groups, get_user_ids_from_groups, set_group_ids_grant_numbers


async def get_users_group(message: types.Message, state: FSMContext):
    if message.text == '/reset':
        await state.finish()
        return
    group_ids_check = await check_users_groups(message.text)

    if not group_ids_check:
        await message.answer('Введите IDs групп пользователей, целые числа, если групп несколько, '
                             'необходимо разделять с помощь запятой (/reset для сброса)')
        return
    data = await state.get_data()
    id_mod_group = data.get('id_mod')

    existed = await get_user_ids_from_groups(mod_group_id=id_mod_group)
    ids_user_groups = message.text
    if existed:
        ids_user_groups += f',{existed[0][0]}'
    text_message = await delete_doubles_ids(ids_user_groups)
    await set_data_groups(values=(id_mod_group, text_message,))

    ids_grant_numbers = await get_ids_for_multiple_record(text_message)
    await set_group_ids_grant_numbers(values=ids_grant_numbers)

    await message.answer('Записал')
    await state.finish()
    await message.answer(text='⚙ Настройка таблицы соответствия групп ⚙',
                         reply_markup=get_conf_groups_kb())


def register_get_users_group(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(get_users_group,
                                chat_type=chat_types,
                                state=Configure.AddUserGroups,
                                is_admin=True)
