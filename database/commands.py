import sqlite3
import os
import datetime

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DB = os.path.join(ROOT_DIR, 'nvn v2.db')
MODERATOR_ID = '1230'

def insert(nickname: str, user_name: str, chat_name: str, user_number: int, dtime:datetime) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_name, user_number, dtime_connetion) VALUES (?, ?, ?, ?,?);
        """, (nickname, user_name, chat_name, user_number, dtime))

def insert2(nickname: str, user_name: str, chat_id: str, user_number: int, dtime:datetime) -> None:
    with sqlite3.connect((DB)) as conn:
        cursor = conn.cursor()
        cursor.execute("""
        INSERT INTO 'users' (nickname, user_name, chat_id, user_number, dtime_connetion) VALUES (?, ?, ?, ?,?);
        """, (nickname, user_name, chat_id, user_number, dtime))

def select():
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM 'users'")
        result = cursor.fetchall()
        return result

def select2():
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        cursor.execute(f"""SELECT group_name, user_name, nickname, user_number, dtime_connetion
                           FROM 'users' JOIN 'user_groups'
                           ON moderator_id = {MODERATOR_ID} AND chat_id = group_id;""")
        result = cursor.fetchall()
        return result
# insert2('katy11', 'katy_p',123,1236,datetime.datetime.now())
# # {НазваниеГруппы} {ИмяУчастника} ({НикУчастника}),
#     # {порядковыйНомерВступления} {ВремяВступления}
#     # “Java разработчик” Василий(ника нет),
#     # 500 26.06.22 10: 56
# vibo = select2()
# for i in vibo:
#  print(i)
# print(datetime.datetime.now())