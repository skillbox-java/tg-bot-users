from aiogram import types
from aiogram.dispatcher.filters import BoundFilter

from tgbot.Utils.DBWorker import get_moder_groups


class IsUserGroup(BoundFilter):

    key = "is_user_group"

    def __init__(self, is_user_group: bool):
        self.is_user_group = is_user_group

    async def check(self, update: types.ChatMemberUpdated) -> dict:
        """
        Фильтр для проверки наличия назначенных групп модераторов и получения их id
        :param update: types.ChatMemberUpdated
        :return: dict
        """
        ids = await get_moder_groups(update.chat.id)
        if ids:
            return {"ids": ids}
