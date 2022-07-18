from aiogram import types
from aiogram.dispatcher.filters import BoundFilter
from aiogram.dispatcher.handler import CancelHandler

from Utils.get_numbers import get_grant_numbers
from tgbot.Utils.DBWorker import get_count_queue


class IsGrantCount(BoundFilter):

    key = "is_grant_count"

    def __init__(self, is_grant_count: bool):
        self.is_grant_count = is_grant_count

    async def check(self, update: types.ChatMemberUpdated):
        config_count = update.bot.data['config'].misc.grant_count
        if not config_count:
            raise CancelHandler()
        count = await update.chat.get_member_count()
        count_to_delete = await get_count_queue(update.chat.id)
        saved_grant_numbers = await get_grant_numbers(update.chat.id)
        if 0 < count_to_delete[0][0] < 3 or (count in saved_grant_numbers) or not count % config_count:
            return {"count": count}
