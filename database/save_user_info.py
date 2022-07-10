from .models import User, UserCounter, db


def insert_user_to_db(message):
    user_id = message.new_chat_members[0].id
    user_name = message.new_chat_members[0].first_name
    group_name = message.chat.title
    user_mention = f'<a href="tg://user?id={user_id}">{user_name}</a>'

    with db:
        instance = User.create(group_name=group_name,
                               user_id=user_id,
                               user_name=user_name,
                               user_mention=user_mention
                               )
        return instance


def insert_counter_and_message_id(**kwargs):
    instance, user_counter, message_id = kwargs.values()
    with db:
        UserCounter.create(owner=instance.id,
                           user_counter=user_counter,
                           message_id=message_id
                           )


def duplicate(user_id):
    return User.get_or_none(user_id=user_id)
