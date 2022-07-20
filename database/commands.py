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
            chat_name: str,
            congr_number: int,
            is_winner: int,
            dtime: datetime = datetime.datetime.now().isoformat()) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_name,
                            congr_number, chat_id, user_id, 
                            dtime_connetion, is_winner) 
                            VALUES (?, ?, ?, ?,?,?,?,?);
        """, (nickname, user_name, chat_name, congr_number, chat_id, user_id, dtime, is_winner))


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


def select_id_from_users(user_id) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT id FROM 'users' WHERE user_id={user_id}")
        record_id = cursor.fetchall()
        return record_id[-1][0]


def temp_save(
            chat_id: int,
            record_id: int,
            bot_message_id: int,
            users_chat: int
            ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'temp_storage' (chat_id, record_id, bot_message_id, button_chat_id) VALUES (?, ?, ?, ?);
        """, (users_chat, record_id, bot_message_id, chat_id,))


def buttons_remover(
        chat_id: int,
        ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT bot_message_id FROM 'temp_storage' WHERE chat_id={chat_id}")
        result = cursor.fetchall()
        delete_list = []
        for i in result:
            delete_list.extend(i)
        return delete_list


def storage_cleaner(
        chat_id: int,
        ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_storage' WHERE chat_id={chat_id};''')


def storage_cleaner_lite(
        message_id: int,
        ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''DELETE FROM 'temp_storage' WHERE bot_message_id={message_id};''')


def is_winner_id_select(
        bot_message_id: int,
        ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        result = cursor.execute(f'''SELECT users.id FROM users Join temp_storage ON users.id=temp_storage.record_id
                        WHERE temp_storage.bot_message_id={bot_message_id};''')
        id = []
        for i in result:
            id.append(i)

        return id[0][0]


def is_winner_record(winner_id: int,
        ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(f'''UPDATE users set is_winner = '1' 
                        WHERE id={winner_id}''')


def other_lucky_check(
        count_users: int,
        chat_id: int,
        ) -> None:
    lucky_number = count_users - count_users % 500
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        result = cursor.execute(f'''SELECT * FROM users WHERE chat_id={chat_id} AND
        (congr_number BETWEEN {lucky_number} AND {lucky_number + 2}) AND is_winner=1;''')
        check_list = []
        for i in result:
            check_list.append(i)


def data_finder(
    bot_message_id: int,
    ) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        result = cursor.execute(f'''SELECT users.user_name, users.congr_number, users.chat_id FROM users Join temp_storage ON users.id=temp_storage.record_id
                        WHERE temp_storage.bot_message_id={bot_message_id};''')
        data = []
        for i in result:
            data.append(i)

        return data
