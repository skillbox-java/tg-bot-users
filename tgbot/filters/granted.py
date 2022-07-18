from aiogram import types
from aiogram.dispatcher.filters import BoundFilter

from tgbot.Utils.DBWorker import check_granted, check_queue


class IsNotGranted(BoundFilter):
    key = "is_not_granted"

    def __init__(self, is_not_granted: bool):
        self.is_not_granted = is_not_granted

    async def check(self, update: types.ChatMemberUpdated):
        granted = await check_granted(user_id=update.new_chat_member.user.id, group_id=update.chat.id)
        if granted[0][0]:
            return False
        in_queue = await check_queue(user_id=update.new_chat_member.user.id, group_id=update.chat.id)
        if not in_queue[0][0]:
            return True
