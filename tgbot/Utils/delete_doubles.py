async def delete_doubles_ids(message: str, sort: bool = False) -> str:
    """
    Функция для удаления дублей из строки с ids групп и сортировки если необходимо
    :param message: str
    :param sort:
    :return: str
    """
    without_doubles = set(message.replace(' ', '').split(','))
    if sort:
        for_sorts = map(int, without_doubles)
        return ','.join(map(str, sorted(for_sorts)))
    else:
        return ','.join(without_doubles)
