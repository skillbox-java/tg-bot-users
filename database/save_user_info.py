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
    with db:
        if len(kwargs) == 3:
            instance, user_counter, message_id = kwargs.values()
            UserCounter.create(owner=instance.id,
                               user_counter=user_counter,
                               message_id=message_id
                               )
        else:
            instance, user_counter = kwargs.values()
            UserCounter.create(owner=instance.id,
                               user_counter=user_counter,
                               )


def duplicate(user_id):
    return User.get_or_none(user_id=user_id)


def write_message_ids(message_ids):
    with db:
        result = (UserCounter
                  .select()
                  .where(UserCounter.message_id == None)
                  )
        for model_obj, ids in zip(result, message_ids):
            model_obj.message_id = ids
            model_obj.save()


def change_deleted_flag(user=None, related_field=None):
    with db:
        if related_field:
            for i in related_field:
                i.deleted = 1
                i.save()
        else:
            query = (UserCounter
                     .select()
                     .where(UserCounter.owner == user.id)
                     ).get()
            query.deleted = 1
            query.save()

