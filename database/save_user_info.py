from .models import User, TechInfo, db


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
            TechInfo.create(owner=instance.id,
                            user_counter=user_counter,
                            message_id=message_id
                            )
        else:
            instance, user_counter = kwargs.values()
            TechInfo.create(owner=instance.id,
                            user_counter=user_counter,
                            )


def duplicate(user_id):
    return User.get_or_none(user_id=user_id)


def write_message_ids_and_restore_deleted_flag(message_ids):
    with db:
        result = (TechInfo
                  .select()
                  .where(TechInfo.congratulated == False)
                  )
        for model_obj, ids in zip(result, message_ids):
            model_obj.message_id = ids
            model_obj.deleted = 0
            model_obj.save()


def change_delete_and_congratulate_flag(user=None, related_field=None, is_congratulated=False):
    with db:
        if related_field:
            for i in related_field:
                if user and is_congratulated and i.owner_id == user.id:
                    i.congratulated = 1
                i.deleted = 1
                i.save()
        else:
            query = (TechInfo
                     .select()
                     .where(TechInfo.owner == user.id)
                     ).get()

            if is_congratulated:
                query.congratulated = 1
            query.deleted = 1
            query.save()

