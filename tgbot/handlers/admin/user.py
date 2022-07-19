from aiogram import Dispatcher
from aiogram.types import Message


async def user_start(message: Message) -> None:
    """
    Функция отправляет приветствие и сообщает, что у пользователя нет прав для конфигурирования
    :param message: Message
    :return: None
    """
    await message.reply(f"Добрый день, у вас нет прав для конфигурирования!")


def register_user(dp: Dispatcher):
    dp.register_message_handler(user_start, commands=["start"], state="*")
