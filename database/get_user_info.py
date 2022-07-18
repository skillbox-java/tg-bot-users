from .models import User, TechInfo, db


def get_user_by_id(user_id):
    with db:
        return (User
                .select(User, TechInfo)
                .join(TechInfo)
                .where(User.user_id == user_id)
                .get()
                )


def get_all():
    with db:
        return (User
                .select(User, TechInfo)
                .join(TechInfo)
                )


def get_message_ids():
    with db:
        return (TechInfo
                .select()
                .where(TechInfo.deleted == False)
                )

def get_not_congratulated_users():
    with db:
        return (User
                .select(User, TechInfo)
                .join(TechInfo)
                .where(TechInfo.congratulated == False)
                )