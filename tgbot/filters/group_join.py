from aiogram import types
from aiogram.dispatcher.filters import BoundFilter


class IsGroupJoin(BoundFilter):
    key = "is_group_join"

    def __init__(self, is_group_join: bool):
        self.is_group_join = is_group_join

    async def check(self, update: types.ChatMemberUpdated):
        return update.new_chat_member.is_chat_member()
