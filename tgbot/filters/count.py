from aiogram import types
from aiogram.dispatcher.filters import BoundFilter
from aiogram.dispatcher.handler import CancelHandler

from tgbot.Utils.DBWorker import get_count_queue, check_data_from_grant_numbers


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
        saved_grant_numbers = await check_data_from_grant_numbers(update.chat.id)
        if count_to_delete[0][0] < 3 or saved_grant_numbers[0][0] or not count % config_count:
            return {"count": count}
        return False
