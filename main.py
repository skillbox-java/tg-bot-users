from database.models import db, User, TechInfo
from loader import bot
import handlers
from utils.set_bot_commands import set_default_commands
from filters.group_filter import IsAdminGroup, IsUsersGroup


def on_startup():
    tables = [User, TechInfo]
    if not all(i.table_exists() for i in tables):
        db.create_tables(tables)

    set_default_commands(bot)
    bot.add_custom_filter(IsAdminGroup())
    bot.add_custom_filter(IsUsersGroup())


if __name__ == '__main__':
    on_startup()
    bot.infinity_polling()
