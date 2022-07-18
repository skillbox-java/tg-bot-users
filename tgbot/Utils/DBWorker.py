import os
import aiosqlite


async def db_execute(string, values=None, multiple=False, get=False) -> bool:
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


async def get_users_groups(group_id):
    return await db_execute(string='SELECT user_group_ids FROM groups WHERE mod_group_id=?', values=(group_id,),
                            get=True)


async def get_moder_groups(group_id):
    return await db_execute(string='SELECT mod_group_id FROM groups WHERE user_group_ids LIKE ?',
                            values=('%'+str(group_id)+'%',),
                            get=True)


async def delete_data_from_groups(ids):
    return await db_execute(string='DELETE FROM groups WHERE id=?', values=ids, multiple=True)


async def get_groups():
    return await db_execute(string='SELECT * FROM groups', get=True)


async def get_user_ids_from_groups(mod_group_id):
    return await db_execute(string='SELECT user_group_ids FROM groups WHERE mod_group_id=?',
                            values=(mod_group_id,),
                            get=True)


async def set_data_groups(values):
    return await db_execute(string='INSERT OR REPLACE INTO groups(mod_group_id, user_group_ids)'
                                   ' VALUES(?, ?);', values=values)


async def set_group_ids_grant_numbers(values):
    return await db_execute(string=
                            "INSERT OR IGNORE INTO grant_numbers(group_id) VALUES(?);", values=values, multiple=True)


async def set_data_queue(values):
    return await db_execute(string=
                            "INSERT INTO queue(message_id, group_id_users, name_group, group_id_mod, user_id, user, "
                            "count, datetime_update, UUID, username) "
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", values=values)


async def update_data_queue(message_id, old_message_id, group_id):
    return await db_execute(
        string='UPDATE queue SET message_id=? WHERE message_id=? AND '
               'group_id_users LIKE ?', values=(message_id, old_message_id, '%'+str(group_id)+'%',))


async def set_data_granted(values):
    return await db_execute(string=
                            "INSERT INTO granted(group_id_users, name_group, user_id, user, group_id_mod, moder_id, "
                            "count, datetime_update, datetime_granted, username) "
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                            values=values, multiple=True)


async def get_data_granted(group_id_moder):
    return await db_execute(string='SELECT * FROM granted WHERE group_id_mod=? ORDER BY datetime_update',
                            values=(group_id_moder,),
                            get=True)


async def get_data_granted_for_kb(group_id_users):
    return await db_execute(
        string='SELECT * FROM granted WHERE group_id_users=? ORDER BY datetime_update', values=(group_id_users,),
        get=True)


async def check_granted(user_id, group_id):
    return await db_execute(
        string="SELECT COUNT(*) FROM granted WHERE user_id=? AND group_id_users=?", values=(user_id, group_id,),
        get=True)


async def get_count_queue(group_id):
    return await db_execute(string="SELECT COUNT(*) FROM queue WHERE group_id_users=?", values=(group_id,),
                            get=True)


async def check_queue(user_id, group_id):
    return await db_execute(
        string="SELECT COUNT(*) FROM queue WHERE user_id=? AND group_id_users=?", values=(user_id, group_id,),
        get=True)


async def get_message_in_queue(uid):
    return await db_execute(string="SELECT * FROM queue WHERE UUID=?", values=(uid,), get=True)


async def get_queue(group_id, message_id=0):
    return await db_execute(
        string="SELECT * FROM queue WHERE group_id_users=? AND message_id!=?", values=(group_id, message_id,),
        get=True)


async def delete_from_queue(group_id):
    return await db_execute(string="DELETE FROM queue WHERE group_id_users=?", values=(group_id,))


async def count_from_queue(group_id):
    return await db_execute(
        string="SELECT count FROM queue WHERE group_id_users=? ORDER BY datetime_update limit 1", values=(group_id,),
        get=True)


async def get_data_from_grant_numbers(group_id):
    return await db_execute(string='SELECT numbers FROM grant_numbers WHERE group_id=?', values=(group_id,), get=True)


async def set_data_numbers(values):
    return await db_execute(string='INSERT OR REPLACE INTO grant_numbers(group_id, numbers)'
                                   ' VALUES(?, ?);', values=values)


async def delete_data_from_grant_numbers(ids):
    return await db_execute(string='DELETE FROM grant_numbers WHERE id=?', values=ids, multiple=True)


async def get_numbers():
    return await db_execute(string='SELECT * FROM grant_numbers', get=True)


async def vacuum():
    return await db_execute(string="VACUUM")
