import sqlite3
import os
import datetime


ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DB = os.path.join(ROOT_DIR, 'nvn v2.db')
MODERATOR_ID = ''
print(MODERATOR_ID, 'before')
def insert(nickname: str, user_name: str, chat_name: str, user_number: int, dtime=datetime.datetime.now().isoformat()) -> None:
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

def select_lucky():
    with sqlite3.connect(( DB )) as conn:
        cursor = conn.cursor()
        print(MODERATOR_ID, 'select1')
        cursor.execute(f"""SELECT group_name, user_name, nickname, user_number, dtime_connetion
                           FROM 'users' JOIN 'user_groups'
                           ON  chat_id = group_id
                           WHERE moderator_id = {MODERATOR_ID};""")
        print(MODERATOR_ID, 'select2')
    result = cursor.fetchall()
    print(result)
    return result

# print(select_lucky())
print(MODERATOR_ID, 'after')
# print(select_lucky())
# insert2('katy11', 'katy_p',123,1236,datetime.datetime.now())
# # {НазваниеГруппы} {ИмяУчастника} ({НикУчастника}),
#     # {порядковыйНомерВступления} {ВремяВступления}
#     # “Java разработчик” Василий(ника нет),
#     # 500 26.06.22 10: 56
# vibo = select_date()
# for i in vibo:
#     dt = datetime.datetime.strptime(i[0], '%Y-%m-%d %H:%M:%S.%f')
#     print(dt.strftime('%d.%m.%y %H:%M'))

