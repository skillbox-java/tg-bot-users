from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType

from Utils.DBWorker import delete_data_from_groups, delete_data_from_grant_numbers
from Utils.check_message_user_groups import check_users_groups
from Utils.get_ids_for_grant_numbers import get_ids_for_multiple_record
from keyboards.inline import get_conf_numbers_kb

from misc.states import Configure


async def delete_numbers(message: types.Message, state: FSMContext):
    if message.text == '/reset':
        await state.finish()
        return
    group_id = await check_users_groups(message.text)
    if not group_id:
        await message.answer('Введите IDs строк для удаления записей из базы, целые числа, '
                             'если нужно удалить несколько, вводите через запятую (/reset для сброса)')
        return
    ids = await get_ids_for_multiple_record(message.text)
    deleted_records = await delete_data_from_grant_numbers(ids)
    if deleted_records:
        await message.answer(f'Удалил {deleted_records} записи(-ей)')
    else:
        await message.answer(f'Таких строк нет в таблице')
    await state.finish()
    await message.answer(text='⚙ Настройка таблицы с поздр. номерами ⚙',
                         reply_markup=await get_conf_numbers_kb())


def register_delete_numbers(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(delete_numbers,
                                chat_type=chat_types,
                                state=Configure.DeleteNumbers,
                                is_admin=True)
