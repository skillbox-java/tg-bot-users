from .models import User, UserCounter, db


def get_user_by_id(user_id):
    with db:
        return (User
                .select(User, UserCounter)
                .join(UserCounter)
                .where(User.user_id == user_id)
                .get()
                )


def get_all():
    with db:
        return (User
                .select(User, UserCounter)
                .join(UserCounter)
                )


def get_message_ids():
    with db:
        return (UserCounter
                .select()
                .where(UserCounter.deleted == False)
                )
