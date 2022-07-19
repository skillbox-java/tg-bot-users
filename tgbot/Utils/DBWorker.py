import os
from typing import List, Union

import aiosqlite


async def db_execute(string: str, values: Union[List[tuple], tuple] = None, multiple: bool = False,
                     get: bool = False) -> Union[List[tuple], int]:
    """Функция для выполнения SQL запросов"""
    db_name = os.getenv('DB_NAME')
    async with aiosqlite.connect(db_name) as db:
        if multiple:
            async with db.executemany(string, values) as cursor:
                await db.commit()
                return cursor.rowcount
        elif get:
            async with db.execute(string, values) as cursor:
                return await cursor.fetchall()
        elif values:
            async with db.execute(string, values) as cursor:
                await db.commit()
                return cursor.rowcount
        else:
            async with db.execute(string) as cursor:
                await db.commit()
                return cursor.rowcount


async def create_tables() -> None:
    """Функция для создания БД и создания таблиц"""

    string = """CREATE TABLE IF NOT EXISTS granted(
               id INTEGER PRIMARY KEY autoincrement,
               group_id_users INTEGER,
               name_group TEXT,
               user_id INTEGER,
               user TEXT,
               group_id_mod INTEGER,
               moder_id INTEGER,
               count,
               datetime_update TEXT,
               datetime_granted TEXT,
               username TEXT);
            """
    await db_execute(string)

    string = """CREATE TABLE IF NOT EXISTS groups(
                   id INTEGER PRIMARY KEY autoincrement,
                   mod_group_id INTEGER UNIQUE,
                   user_group_ids TEXT);
                """
    await db_execute(string)

    string = """CREATE TABLE IF NOT EXISTS queue(
                       id INTEGER PRIMARY KEY autoincrement,
                       message_id INTEGER,
                       group_id_users INTEGER,
                       name_group TEXT,
                       group_id_mod INTEGER,
                       user_id INTEGER,
                       user TEXT,
                       count INTEGER,
                       datetime_update TEXT,
                       UUID TEXT,
                       username TEXT);
                    """
    await db_execute(string)

    string = """CREATE TABLE IF NOT EXISTS grant_numbers(
                           id INTEGER PRIMARY KEY autoincrement,
                           group_id INTEGER UNIQUE,
                           numbers TEXT);
                        """
    await db_execute(string)


async def get_users_groups(group_id: int) -> List[tuple]:
    """
    Функция для получения ids модерируемых групп из таблицы groups
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(string='SELECT user_group_ids FROM groups WHERE mod_group_id=?', values=(group_id,),
                            get=True)


async def get_moder_groups(group_id: int) -> List[tuple]:
    """
    Функция для получения ids групп модераторов из таблицы groups
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(string='SELECT mod_group_id FROM groups WHERE user_group_ids LIKE ?',
                            values=('%' + str(group_id) + '%',),
                            get=True)


async def delete_data_from_groups(ids: List[tuple]) -> int:
    """
    Функция для удаления записей из таблицы groups
    :param ids: List[tuple]
    :return: int
    """
    return await db_execute(string='DELETE FROM groups WHERE id=?', values=ids, multiple=True)


async def get_groups() -> List[tuple]:
    """
    Функция для получения всех данных из таблицы groups
    :return: List[tuple]
    """
    return await db_execute(string='SELECT * FROM groups', get=True)


async def set_data_groups(values: tuple) -> int:
    """
    Функция для вставки новой записи в таблицу groups, или замены
    :param values:
    :return: int
    """
    return await db_execute(string='INSERT OR REPLACE INTO groups(mod_group_id, user_group_ids)'
                                   ' VALUES(?, ?);', values=values)


async def set_group_ids_grant_numbers(values: List[tuple]) -> int:
    """
    Функция для вставки новой записи в таблицу grant_numbers, или замены
    :param values: tuple
    :return: int
    """
    return await db_execute(string=
                            "INSERT OR IGNORE INTO grant_numbers(group_id) VALUES(?);", values=values, multiple=True)


async def set_data_queue(values: tuple) -> int:
    """
    Функция для вставки новой записи в таблицу queue
    :param values: tuple
    :return: int
    """
    return await db_execute(string=
                            "INSERT INTO queue(message_id, group_id_users, name_group, group_id_mod, user_id, user, "
                            "count, datetime_update, UUID, username) "
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", values=values)


async def update_data_queue(message_id: int, old_message_id: int, group_id: int) -> int:
    """
    Функция для обновления записи в таблице queue
    :param message_id: int
    :param old_message_id: int
    :param group_id: int
    :return: int
    """
    return await db_execute(
        string='UPDATE queue SET message_id=? WHERE message_id=? AND '
               'group_id_users LIKE ?', values=(message_id, old_message_id, '%' + str(group_id) + '%',))


async def set_data_granted(values: List[tuple]) -> int:
    """
    Функция для вставки новых записей в таблицу granted
    :param values: List[tuple]
    :return: int
    """
    return await db_execute(string=
                            "INSERT INTO granted(group_id_users, name_group, user_id, user, group_id_mod, moder_id, "
                            "count, datetime_update, datetime_granted, username) "
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                            values=values, multiple=True)


async def get_data_granted(group_id_moder: int) -> List[tuple]:
    """
    Функция для получения данных из таблицы granted с отбором по полю group_id_mod
    :param group_id_moder: int
    :return: List[tuple]
    """
    return await db_execute(string='SELECT * FROM granted WHERE group_id_mod=? ORDER BY datetime_update',
                            values=(group_id_moder,),
                            get=True)


async def get_data_granted_for_kb(group_id_users: int) -> List[tuple]:
    """
    Функция для получения данных из таблицы granted отсортированных по datetime_update и отборо по group_id_users
    :param group_id_users: int
    :return: List[tuple]
    """
    return await db_execute(
        string='SELECT * FROM granted WHERE group_id_users=? ORDER BY datetime_update', values=(group_id_users,),
        get=True)


async def check_granted(user_id: int, group_id: int) -> List[tuple]:
    """
    Функция для получения данных из таблицы granted c отбором по group_id_users и user_id
    :param user_id: int
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(
        string="SELECT COUNT(*) FROM granted WHERE user_id=? AND group_id_users=?", values=(user_id, group_id,),
        get=True)


async def get_count_queue(group_id: int) -> List[tuple]:
    """
     Функция для получения данных из таблицы queue c отбором по group_id_users
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(string="SELECT COUNT(*) FROM queue WHERE group_id_users=?", values=(group_id,),
                            get=True)


async def check_queue(user_id: int, group_id: int) -> List[tuple]:
    """
    Функция для получения кол-ва существующих записей с отбором по user_id и group_id_users
    :param user_id: int
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(
        string="SELECT COUNT(*) FROM queue WHERE user_id=? AND group_id_users=?", values=(user_id, group_id,),
        get=True)


async def get_message_in_queue(uid: str) -> List[tuple]:
    """
    Функция для получения все данных из таблицы queue с отбором по UUID
    :param uid: str
    :return: List[tuple]
    """
    return await db_execute(string="SELECT * FROM queue WHERE UUID=?", values=(uid,), get=True)


async def get_queue(group_id: int, message_id: int = 0) -> List[tuple]:
    """
    Функция для получения всех данных из таблицы queue с отбором по group_id_users и message_id
    :param group_id: int
    :param message_id: int
    :return: List[tuple]
    """
    return await db_execute(
        string="SELECT * FROM queue WHERE group_id_users=? AND message_id!=?", values=(group_id, message_id,),
        get=True)


async def delete_from_queue(group_id: int) -> List[tuple]:
    """
    Функция для получения все данных из таблицы queue с отбором по group_id_users
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(string="DELETE FROM queue WHERE group_id_users=?", values=(group_id,))


async def count_from_queue(group_id: int) -> List[tuple]:
    """
      Функция для проверки существования записи с отбором по group_id_users
      :param group_id: int
      :return: List[tuple]
      """
    return await db_execute(
        string="SELECT count FROM queue WHERE group_id_users=? ORDER BY datetime_update limit 1", values=(group_id,),
        get=True)


async def get_data_from_grant_numbers(group_id: int) -> List[tuple]:
    """
    Функция для получения поля numbers из таблицы grant_numbers с отбором по group_id
    :param group_id: int
    :return: List[tuple]
    """
    return await db_execute(string='SELECT numbers FROM grant_numbers WHERE group_id=?', values=(group_id,), get=True)


async def set_data_numbers(values: tuple) -> int:
    """
    Функция для вставки новой записи в grant_numbers, или замены существующей
    :param values: tuple
    :return: int
    """
    return await db_execute(string='INSERT OR REPLACE INTO grant_numbers(group_id, numbers)'
                                   ' VALUES(?, ?);', values=values)


async def delete_data_from_grant_numbers(ids: List[tuple]) -> int:
    """
    Функция для удаления записей из grant_numbers с отбором по id
    :param ids: List[tuple]
    :return: int
    """
    return await db_execute(string='DELETE FROM grant_numbers WHERE id=?', values=ids, multiple=True)


async def get_numbers() -> List[tuple]:
    """
    Функция для получения всех данных из grant_numbers
    :return: List[tuple]
    """
    return await db_execute(string='SELECT * FROM grant_numbers', get=True)


async def vacuum() -> int:
    """
    Пылесос:)
    :return: int
    """
    return await db_execute(string="VACUUM")
