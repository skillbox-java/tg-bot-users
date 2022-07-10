from loader import bot
import handlers
from utils.set_bot_commands import set_default_commands
from filters.group_filter import IsAdminGroup, IsUsersGroup

if __name__ == '__main__':
    set_default_commands(bot)
    bot.add_custom_filter(IsAdminGroup())
    bot.add_custom_filter(IsUsersGroup())
    bot.infinity_polling()
