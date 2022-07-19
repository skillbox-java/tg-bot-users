from aiogram import Dispatcher
from aiogram.types import Message


async def admin_start(message: Message) -> None:
    """
    Если админ отправляет команду /start, бот здоровается и предлагает воспользоваться меню
    :param message: Message
    :return: None
    """
    await message.reply("Добрый день! Конфигурация доступна из меню.")


def register_admin(dp: Dispatcher):
    dp.register_message_handler(admin_start, commands=["start"], state="*", is_admin=True)
