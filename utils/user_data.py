from datetime import datetime


class UsersData:
    COUNTER = 0
    users_info = dict()

    def __init__(self, message):
        self.user_id = message.new_chat_members[0].id
        self.group_name = message.chat.title
        self.current_time = datetime.now().strftime("%d.%m.%Y %H:%M:%S")
        self.user_name = message.new_chat_members[0].first_name
        self.user_mention = f'<a href="tg://user?id={self.user_id}">{self.user_name}</a>'

        self.message_id = None
        self.user_number = None

        UsersData.add_user(self.user_id, self)

    @staticmethod
    def get_user(user_id=None, message=None):
        if message:
            user_id = message.new_chat_members[0].id
        else:
            user_id = user_id
        if UsersData.users_info.get(user_id) is None:
            new_user = UsersData(message)
            return new_user
        return UsersData.users_info.get(user_id)

    @classmethod
    def add_user(cls, user_id, user):
        cls.users_info[user_id] = user
