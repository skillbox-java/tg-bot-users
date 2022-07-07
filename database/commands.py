import sqlite3


def insert(nickname: str, user_name: str, chat_name: str) -> None:
    with sqlite3.connect(('nvn.db')) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_name) VALUES (?, ?, ?);
        """, (nickname, user_name, chat_name))


def select():
    with sqlite3.connect(('nvn.db')) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM 'users'")
        result = cursor.fetchall()
        return result
