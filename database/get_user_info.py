from .models import User, UserCounter, db


def get_user_by_id(user_id):
    with db:
        return (User
                .select(User, UserCounter)
                .join(UserCounter)
                .where(User.user_id == user_id)
                .get()
                )
