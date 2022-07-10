from telebot.custom_filters import ChatFilter


class IsAdminGroup(ChatFilter):
    key = 'is_admin_group'

    def check(self, message, group_ids):
        return message.chat.id in group_ids


class IsUsersGroup(ChatFilter):
    key = 'is_users_group'

    def check(self, message, group_ids):
        return message.chat.id in group_ids
