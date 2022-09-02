from contextlib import suppress

from aiogram import Dispatcher, types
from aiogram.types import ChatType
from aiogram.utils.exceptions import MessageCantBeDeleted

from tgbot.misc.states import Configure


async def delete_groups(call: types.CallbackQuery) -> None:
    """
    Функция коллбека для удаления записей из таблицы соотв. групп
    :param call: types.CallbackQuery
    :return: None
    """
    with suppress(MessageCantBeDeleted):
        await call.message.delete()

    await call.message.answer('Введите IDs строк для удаления записей из базы, целые числа, '
                              'если нужно удалить несколько, вводите через запятую (/reset для сброса)')
    await Configure.DeleteUserGroups.set()


def register_delete_groups_cb(dp: Dispatcher):
    chat_types = [ChatType.PRIVATE]
    dp.register_callback_query_handler(delete_groups,
                                       chat_type=chat_types,
                                       text='delete_groups',
                                       state="*",
                                       is_admin=True)
