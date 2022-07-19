from aiogram import types


async def send_granted_message(granted_list: list[tuple], message: types.Message) -> None:
    """
    Функция для отправкий сообщений с юбилейными пользователями
    :param granted_list: list[tuple]
    :param message: types.Message
    :return: None
    """
    for granted in granted_list:
        emoji = '🎉'
        if granted[6]:
            emoji = '👑👑👑'
        await message.answer(text=f'{emoji} “{granted[2]}” 👤 {granted[4]} ({granted[10]}),\n'
                                  f'🔢 {granted[7]} 🕐 {granted[8]}')
