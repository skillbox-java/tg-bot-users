from aiogram import types


async def set_default_commands(dp):

    await dp.bot.set_my_commands([
        types.BotCommand("start", "Запустить бота"),
        types.BotCommand("/configure_groups", "Настроить группы"),
        types.BotCommand("/configure_grant_num", "Настроить номера для поздравлений")
    ])
