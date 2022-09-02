from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType

from tgbot.Utils.check_ids_records import check_ids
from tgbot.Utils.check_message_user_groups import check_users_groups
from tgbot.Utils.delete_doubles import delete_doubles_ids
from tgbot.keyboards.inline import get_conf_numbers_kb
from tgbot.misc.states import Configure
from tgbot.Utils.DBWorker import get_data_from_grant_numbers, set_data_numbers


async def get_grant_numbers(message: types.Message, state: FSMContext) -> None:
    """
    Функция для ввода номеров для поздрв. для записи в таблицу БД
    :param message: types.Message
    :param state: FSMContext
    :return: None
    """
    if message.text == '/reset':
        await state.finish()
        return
    group_ids_check = await check_ids(message.text)

    if not group_ids_check:
        await message.answer('Введите номера для поздравления, несколько номеров'
                             ' необходимо разделить через запятую (/reset для сброса)')
        return
    data = await state.get_data()
    id_group = data.get('id_group')

    existed = await get_data_from_grant_numbers(group_id=id_group)
    numbers = message.text
    if existed:
        numbers += f',{existed[0][0]}'
    text_message = await delete_doubles_ids(message=numbers, sort=True)

    await set_data_numbers(values=(id_group, text_message,))

    await message.answer('Записал')
    await state.finish()
    await message.answer(text='⚙ Настройка таблицы с поздр. номерами ⚙',
                         reply_markup=get_conf_numbers_kb())


def register_get_grant_numbers(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(get_grant_numbers,
                                chat_type=chat_types,
                                state=Configure.AddNumbers,
                                is_admin=True)
