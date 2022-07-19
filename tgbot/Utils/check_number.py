async def check_number_in_message(number: str) -> int:
    """
    Функция для проверки ids групп на целое число
    :param number: str
    :return: int
    """
    if number[:1] == '-':
        if number[1:].isdigit():
            return int(number)
    elif number.isdigit():
        return int(number)
