from aiogram import types
from aiogram.dispatcher.filters import BoundFilter

from tgbot.Utils.DBWorker import get_users_groups


class IsModerGroup(BoundFilter):
    """
    Фильтр для проверки наличия назначенных групп пользователей и получения их id
    """
    key = "is_moder_group"

    def __init__(self, is_moder_group: bool):
        self.is_moder_group = is_moder_group

    async def check(self, message: types.Message):
        ids = await get_users_groups(message.chat.id)
        if ids:
            return {"ids": ids}
        return False
