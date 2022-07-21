def get_great_text(user: str, count: int) -> str:
    """
    Функция возврата текста для поздравления
    :param user: str
    :param count: int
    :return: str
    """
    text = f'🎉 Поздравляю, {user}, как же удачно попали в нужное место и в нужное время!\n' \
           f'Вы {count} участник комьюнити.\n' \
           f'Вас ждут плюшки и печенюшки!🎉'
    return text
