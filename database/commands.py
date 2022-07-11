import sqlite3
import os

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DB = os.path.join(ROOT_DIR, 'nvn.db')

def insert(nickname: str, user_name: str, user_number: int, dtime:str, chat_id: int, is_winer: int) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, user_number, dtime_connetion, chat_id, is_winer) VALUES (?, ?, ?, ?, ?,?);
        """, (nickname, user_name, user_number, dtime, chat_id, is_winer))


def select():
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