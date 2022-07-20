import sqlite3
import os
from typing import List
from config_data.config import HAPPY_NUMBER


ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DB = os.path.join(ROOT_DIR, 'database.db')


""""Блок функций для регистрации и обработки нового юбилейного пользователя"""


def get_all_group_id() -> List[int]:
    """
    Функция, которая возвращает список всех id групп пользователей из таблицы groups_relation.
    :return List[int]: список id групп пользователей
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT DISTINCT group_id FROM 'groups_relation'")
        group_id_list = cursor.fetchall()
        result = []
        for group_id in group_id_list:
            result.append(group_id[0])
        return result


def get_moderator_id(group_id: int) -> List[int]:
    """
    Функция, которая возвращает список id групп модераторов из таблицы groups_relation по id группы пользователя.
    :param int group_id: уникальный id группы пользователя
    :return List[int]: список id групп модераторов
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT DISTINCT moderator_id FROM 'groups_relation' WHERE group_id={group_id}")
        moderator_id_list = cursor.fetchall()
        result = []
        for moderator_id in moderator_id_list:
            result.append(moderator_id[0])
        return result


def insert_to_users(nickname: str, user_name: str, chat_id: int, user_id: int, chat_name: str,
                    congr_number: int, is_winner: int, dtime_connection) -> None:
    """
    Функция, которая записывает nickname, user_name, chat_id, user_id, chat_name, congr_number, is_winner, dtime в
    таблицу users при вступлении нового юбилейного пользователя.
    :param str nickname: ник пользователя в telegram
    :param str user_name: имя пользователя в telegram
    :param int chat_id: уникальный id группы, куда вступил новый пользователь
    :param int user_id: уникальный id пользователя
    :param str chat_name: имя чата, куда вступил новый пользователь
    :param int congr_number: юбилейный номер вступления пользователя
    :param int is_winner: является ли пользователь победителем (1 да, 0 нет)
    :param dtime_connection: дата и время вступления пользователя
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_name,
                            congr_number, chat_id, user_id, 
                            dtime_connetion, is_winner) 
                            VALUES (?, ?, ?, ?,?,?,?,?);
        """, (nickname, user_name, chat_name, congr_number, chat_id, user_id, dtime_connection, is_winner))


def winner_check(user_id: int) -> list:
    """"
    Функция которая проверяет, является ли пользователь из таблицы users c конкретным значением user_id победителем.
    :param int user_id: уникальный id пользователя
    :return list: информация о пользователе-победителе в виде списка
    """
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT * FROM users WHERE user_id={user_id} AND is_winner=1")
        result = cursor.fetchall()
        return result


def other_lucky_check(count_users: int, chat_id: int) -> list:
    """"
    Функция которая проверяет, есть ли в таблице users для данного chat_id группы и данного юбилейного номера,
    победители.
    :param int count_users: юбилейный номер пользователя
    :param int chat_id: уникальный id группы
    :return list: информация о победителях
    """
    lucky_number = count_users - count_users % HAPPY_NUMBER
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''SELECT * FROM users WHERE chat_id={chat_id} AND
        (congr_number BETWEEN {lucky_number} AND {lucky_number + 2}) AND is_winner=1;''')
        winners_list = cursor.fetchall()
        result = []
        for winner in winners_list:
            result.append(winner)
        return result


def select_id_from_users(user_id: int) -> int:
    """"
    Функция которая по id пользователя возвращает id последней записи о нем из таблицы 'users'.
    :param int user_id: уникальный id пользователя
    :return int: id записи о пользователе
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT id FROM 'users' WHERE user_id={user_id}")
        record_id = cursor.fetchall()
        if record_id:
            return record_id[-1][0]
        return 0


def temp_save(chat_id: int, record_id: int, bot_message_id: int) -> None:
    """"
    Функция которая в таблицу 'temp_storage' записывает chat_id, record_id, bot_message_id.
    :param int chat_id: уникальный id группы
    :param int record_id: уникальный id записи о пользователе в таблицу users
    :param int bot_message_id: уникальный id сообщения бота
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'temp_storage' (chat_id, record_id, bot_message_id) VALUES (?, ?, ?);
        """, (chat_id, record_id, bot_message_id))


def is_winner_id_select(bot_message_id: int) -> int:
    """"
    Функция которая по bot_message_id сообщенияб бота возвращает id  записи о пользователе из таблицы 'temp_storage'.
    :param int bot_message_id: уникальный id сообщения бота
    :return int: id записи о пользователе
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        ids = cursor.execute(f'''SELECT users.id FROM users Join temp_storage ON users.id=temp_storage.record_id
                        WHERE temp_storage.bot_message_id={bot_message_id};''')
        result = []
        for id in ids:
            result.append(id)
        return result[0][0]


def data_finder(bot_message_id: int) -> list:
    """"
    Функция которая по bot_message_id сообщения бота возвращает user_name, congr_number, chat_id  из таблицы 'users'.
    :param int bot_message_id: уникальный id сообщения бота
    :return list: информация о победите
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        result = cursor.execute(f'''SELECT users.user_name, users.congr_number, users.chat_id FROM users 
                        Join temp_storage ON users.id=temp_storage.record_id
                        WHERE temp_storage.bot_message_id={bot_message_id};''')
        data = []
        for i in result:
            data.append(i)
        return data


def is_winner_record(winner_id: int) -> None:
    """"
    Функция которая по winner_id id записи о пользователе в таблице 'users' обновляет поле is_winner с 0 на 1.
    :param int winner_id: уникальный id записи о пользователе в таблице 'users'
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''UPDATE users set is_winner = '1' 
                        WHERE id={winner_id}''')


def buttons_remover(chat_id: int) -> List[int]:
    """"
    Функция которая по chat_id группы пользователей из таблицы 'temp_storage' возвращает все bot_message_id.
    :param int chat_id: уникальный id группы пользователей
    :return List[int]: список id bot_message_id
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT bot_message_id FROM 'temp_storage' WHERE chat_id={chat_id}")
        result = cursor.fetchall()
        delete_list = []
        for i in result:
            delete_list.append(i)
        return delete_list


def storage_cleaner(chat_id: int) -> None:
    """"
    Функция которая по chat_id группы пользователей из таблицы 'temp_storage' удаляет все записи.
    :param int chat_id: уникальный id записи о пользователе в таблице 'users'
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_storage' WHERE chat_id={chat_id};''')


def storage_cleaner_lite(bot_message_id: int) -> None:
    """"
    Функция которая по bot_message_id сообщения бота из таблицы 'temp_storage' удаляет запись.
    :param int bot_message_id: уникальный id сообщения бота
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_storage' WHERE bot_message_id={bot_message_id};''')


""""Блок функций для настроек и просмотра админки"""


def insert_to_groups(moderator_id: int, group_id: int) -> None:
    """
    Функция, которая записывает id группы модераторов и групп пользователей в таблицу groups_relation (по связи многие
    ко многим; одной записи соответствует одна пара).
    :param moderator_id: id группы модераторов
    :param group_id: id группы пользователей
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(
            """INSERT INTO 'groups_relation' (moderator_id, group_id) 
            VALUES (?, ?);""",
            (moderator_id, group_id)
        )


def select_from_groups() -> List[tuple]:
    """
    Генератор, который из таблицы groups_relation возвращает данные о записях id групп модераторов и пользователей,
    сгруппированные по id групп модераторов.
    :return List[tuple]: id групп модераторов и пользователей
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT DISTINCT moderator_id "
                       "FROM 'groups_relation'")
        moderator_id_list = cursor.fetchall()
        for moderator_id in moderator_id_list:
            cursor.execute(f"SELECT moderator_id, group_id "
                           f"FROM 'groups_relation'"
                           f"WHERE moderator_id={moderator_id[0]}")
            result = cursor.fetchall()
            yield result


def delete_from_groups() -> None:
    """
    Функция, которая полностью очищает таблицу groups_relation.
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("DELETE FROM 'groups_relation'")


def get_all_moderator_id() -> List[int]:
    """
    Функция, которая возвращает список всех id групп модераторов из таблицы groups_relation.
    :return List[int]: список id групп модераторов
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT DISTINCT moderator_id FROM 'groups_relation'")
        moderator_id_list = cursor.fetchall()
        result = []
        for moderator_id in moderator_id_list:
            result.append(moderator_id[0])
        return result


"блок функций для вывода всех кто в юбилейном списке и для поздравления последних непоздравленных пользователей"


def select_lucky(moderator_id: int) -> list:
    """
    Функция, которая возвращает chat_name, user_name, nickname, congr_number, dtime_connetion, is_winner, chat_id
    из таблицы users по id группы модератора.
    :param int moderator_id: уникальный id группы модератора
    :return list: список информации обо всех зафиксированных пользователях
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"""SELECT chat_name, user_name, nickname, congr_number, dtime_connetion, is_winner, chat_id
                           FROM 'users' JOIN 'groups_relation'
                           ON chat_id = group_id AND moderator_id ={moderator_id};""")
    result = cursor.fetchall()
    return result


def get_group_id(moderator_id: int) -> List[int]:
    """
    Функция, которая возвращает список id групп пользователей из таблицы groups_relation по id группы модератора.
    :param int moderator_id: уникальный id группы модератора
    :return List[int]: список id групп пользователей
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT DISTINCT group_id FROM 'groups_relation' WHERE moderator_id={moderator_id}")
        group_id_list = cursor.fetchall()
        result = []
        for group_id in group_id_list:
            result.append(group_id[0])
        return result


def select_last_congr_number_from_users(chat_id: int) -> int:
    """"
    Функция которая по chat_id группы пользователя возвращает congr_number последней записи из таблицы 'users'.
    :param int chat_id: уникальный id пользователя
    :return int: congr_number последней записи
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT congr_number FROM 'users' WHERE user_id={chat_id}")
        record_id = cursor.fetchall()
        if record_id:
            return record_id[-1][0]
        return 0


def is_uncongr(congr_number: int, chat_id: int) -> bool:
    """"
    Функция которая проверяет, есть ли в таблице users для данного chat_id группы и данного юбилейного номера,
    непоздравленные пользователи.
    :param int congr_number: юбилейный номер пользователя
    :param int chat_id: уникальный id группы
    :return bool: True если последние пользователи не поздравлены, False если последние пользователи поздравлены
    """
    lucky_number = congr_number - congr_number % HAPPY_NUMBER
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''SELECT is_winner
                           FROM 'users' WHERE chat_id={chat_id} AND 
                           (congr_number BETWEEN {lucky_number} AND {lucky_number + 2})''')
        winners_list = cursor.fetchall()
        for winner in winners_list:
            if winner[0] == 1:
                return False
        return True


def select_last_uncongratulate(congr_number: int, chat_id: int) -> list:
    """"
    Функция которая проверяет, есть ли в таблице users для данного chat_id группы и данного юбилейного номера,
    непоздравленные пользователи.
    :param int congr_number: юбилейный номер пользователя
    :param int chat_id: уникальный id группы
    :return list: информация о победителях
    """
    lucky_number = congr_number - congr_number % HAPPY_NUMBER
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''SELECT chat_id, congr_number, nickname, user_name, chat_name, dtime_connetion, id
                           FROM 'users' WHERE chat_id={chat_id} AND 
                           (congr_number BETWEEN {lucky_number} AND {lucky_number + 2})''')
        winners_list = cursor.fetchall()
        result = []
        for winner in winners_list:
            result.append(winner)
        return result


def temp_save_unceleb(chat_id: int, record_id: int, bot_message_id: int) -> None:
    """
    Функция, которая записывает chat_id, record_id, bot_message_id в таблицу 'temp_unceleb'.
    :param int chat_id: id группы пользователей
    :param int record_id: id записи о пользователе
    :param int bot_message_id: id сообщения бота
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'temp_unceleb' (chat_id, record_id, bot_message_id) VALUES (?, ?, ?);
        """, (chat_id, record_id, bot_message_id))


def temp_cleaner() -> None:
    """
    Функция, которая очищает таблицу 'temp_unceleb'.
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_unceleb';''')


def get_chat_id_unceleb(bot_message_id) -> int:
    """
    Функция, которая по bot_message_id сообщения бота возвращает id группы пользователя.
    :param int bot_message_id: id сообщения бота
    :return int: id группы пользователя
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT chat_id FROM 'temp_unceleb' WHERE bot_message_id={bot_message_id}")
        result = cursor.fetchall()
        return result[0][0]


def is_winner_id_select_unceleb(bot_message_id: int) -> int:
    """
    Функция, которая по bot_message_id сообщения бота возвращает id записи о пользователе.
    :param int bot_message_id: id сообщения бота
    :return int: id записи о пользователе
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        result = cursor.execute(f'''SELECT id FROM users Join temp_unceleb ON id=record_id
                        WHERE bot_message_id={bot_message_id};''')
        id = []
        for i in result:
            id.append(i)
        return id[0][0]


def buttons_remover_unceleb(chat_id: int) -> List[int]:
    """
    Функция, которая по chat_id группы пользователя возвращает список bot_message_id сообщений бота из
    таблицы 'temp_unceleb'.
    :param int chat_id: id группы пользователя
    :return List[int]: список bot_message_id сообщений бота
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT bot_message_id FROM 'temp_unceleb' WHERE chat_id={chat_id}")
        result = cursor.fetchall()
        delete_list = []
        for i in result:
            delete_list.append(i)
        return delete_list


def storage_cleaner_unceleb(chat_id: int) -> None:
    """
    Функция, которая по chat_id группы пользователя очищает таблицу 'temp_unceleb'.
    :param int chat_id: id группы пользователя
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_unceleb' WHERE chat_id={chat_id};''')


def record_cleaner_unceleb(bot_message_id: int) -> None:
    """
    Функция, которая по bot_message_id сообщения бота очищает запись в таблице 'temp_unceleb'.
    :param int bot_message_id: id сообщения бота
    :return: None
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_unceleb' WHERE bot_message_id={bot_message_id};''')
