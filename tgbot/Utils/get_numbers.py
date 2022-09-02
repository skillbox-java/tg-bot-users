from typing import List

from tgbot.Utils.DBWorker import get_data_from_grant_numbers


async def get_grant_numbers(group_id: int) -> List[int]:
    """
    Возвращает данные о юбилейных пользователях для фильтра
    :param group_id: int
    :return: List[int]
    """
    numbers = await get_data_from_grant_numbers(group_id=group_id)
    if not numbers[0][0]:
        return []
    return [int(number) for number in numbers[0][0].split(',')]
