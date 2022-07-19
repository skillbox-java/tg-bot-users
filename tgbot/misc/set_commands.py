from aiogram import types, Dispatcher


async def set_default_commands(dp: Dispatcher) -> None:
    """
    Функция для установки комманд меню в приватном канале бота
    :param dp: Dispatcher
    :return: None
    """

    await dp.bot.set_my_commands([
        types.BotCommand("start", "Запустить бота"),
        types.BotCommand("/configure", "Конфигурация"),
    ])
