import sqlite3
import os

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DB = os.path.join(ROOT_DIR, 'nvn.db')

def insert(nickname: str, user_name: str, chat_name: str, user_number: int, dtime:str) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_name, user_number, dtime_connetion) VALUES (?, ?, ?, ?,?);
        """, (nickname, user_name, chat_name, user_number, dtime))

def insert_lite(nickname: str, user_name: str, chat_name: str) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute(""" INSERT INTO 'users' (nickname, user_name, chat_name) 
        VALUES (nickname, user_name, chat_name) """)

def select():
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM 'users'")
        result = cursor.fetchall()
        return result