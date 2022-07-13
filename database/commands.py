import sqlite3
import os
import datetime
from typing import List


ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DB = os.path.join(ROOT_DIR, 'database.db')


def insert(nickname: str, user_name: str, chat_name: str, user_number: int, dtime=datetime.datetime.now().isoformat()) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_name, user_number, dtime_connetion) VALUES (?, ?, ?, ?,?);
        """, (nickname, user_name, chat_name, user_number, dtime))


def insert2(nickname: str,
            user_name: str,
            chat_id: int,
            user_id: int,
            is_winner: int,
            dtime: datetime = datetime.datetime.now().isoformat()) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_id, user_id, dtime_connetion, is_winner) VALUES (?, ?, ?, ?,?,?);
        """, (nickname, user_name, chat_id, user_id, dtime, is_winner))


def select_lucky(moderator_id):
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        # print(moderator_id, 's')
        cursor.execute(f"""SELECT chat_name, user_name, nickname, congr_number, dtime_connetion, is_winner
                           FROM 'users' JOIN 'groups_relation'
                           ON chat_id = group_id AND moderator_id = abs({moderator_id});""")
        # print(moderator_id, 'select2')
    result = cursor.fetchall()
    # prin(r)
    return result


def winner_check(id):
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT * FROM users WHERE user_id={id} AND is_winner=1")
        result = cursor.fetchall()
        return result


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


def get_moderator_id() -> List[int]:
    """
    Функция, которая возвращает список id групп модераторов из таблицы groups_relation.
    :return List[int]: список id групп модераторов
    """
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT moderator_id FROM 'groups_relation'")
        moderator_id_list = cursor.fetchall()
        result = []
        for moderator_id in moderator_id_list:
            result.append(moderator_id[0])
        return result
