from typing import List


async def get_ids_for_multiple_record(ids: str) -> List[tuple]:
    """
    Функция формирует список с кортежами с ids групп для работы с БД
    :param ids: str
    :return: List[tuple]
    """
    return [(group_id,) for group_id in ids.split(',')]
