from loader import bot
import handlers
from utils.set_bot_commands import set_default_commands

from database.commands import cleaner

if __name__ == '__main__':

    cleaner()

    set_default_commands(bot)
    bot.infinity_polling()
    # ТЗ: Используйте LongPolling вариант работы с серверами телеграм
