from tgbot.Utils.check_number import check_number_in_message


async def check_users_groups(message: str) -> bool:
    """
    Функция для проверки ids групп на корректность
    :param message: str
    :return: bool
    """
    users_groups_ids = message.split(',')
    for number in users_groups_ids:
        if await check_number_in_message(number=number) and len(number) > 4:
            return True

