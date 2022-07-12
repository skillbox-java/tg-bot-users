import sqlite3
import os
import datetime


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
            user_number: int,
            dtime: datetime = datetime.datetime.now().isoformat()) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_id, user_number, dtime_connetion) VALUES (?, ?, ?, ?,?);
        """, (nickname, user_name, chat_id, user_number, dtime))


def select_lucky():
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM 'users'")
        result = cursor.fetchall()
        return result


def winner_check(id):
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute(f"SELECT is_winer FROM users WHERE is_winer={id}")
        result = cursor.fetchall()
        return result


def insert_to_groups(group_id: int, moderator_id: int) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'user_groups' (group_id, moderator_id) VALUES (?, ?);
        """, (group_id, moderator_id))


def select_from_groups():
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM 'user_groups'")
        result = cursor.fetchall()
        return result
