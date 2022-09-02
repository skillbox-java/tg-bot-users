from aiogram import Dispatcher, types
from aiogram.dispatcher import FSMContext
from aiogram.types import ChatType

from tgbot.Utils.DBWorker import delete_data_from_groups, vacuum
from tgbot.Utils.check_ids_records import check_ids
from tgbot.Utils.get_ids_for_grant_numbers import get_ids_for_multiple_record
from tgbot.keyboards.inline import get_conf_groups_kb

from tgbot.misc.states import Configure


async def delete_from_groups(message: types.Message, state: FSMContext) -> None:
    """
    Функция для удаления записей из таблицы групп
    :param message: types.Message
    :param state: FSMContext
    :return: None
    """
    if message.text == '/reset':
        await state.finish()
        return
    record_id = await check_ids(message.text)
    if not record_id:
        await message.answer('Введите IDs строк для удаления записей из базы, целые числа, '
                             'если нужно удалить несколько, вводите через запятую (/reset для сброса)')
        return
    ids = await get_ids_for_multiple_record(message.text)
    deleted_records = await delete_data_from_groups(ids)
    if deleted_records:
        await message.answer(f'Удалил {deleted_records} записи(-ей)')
        await vacuum()
    else:
        await message.answer(f'Таких строк нет в таблице')
    await state.finish()
    await message.answer(text='⚙ Настройка таблицы соответствия групп ⚙',
                         reply_markup=get_conf_groups_kb())


def register_delete_from_groups(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_message_handler(delete_from_groups,
                                chat_type=chat_types,
                                state=Configure.DeleteUserGroups,
                                is_admin=True)
