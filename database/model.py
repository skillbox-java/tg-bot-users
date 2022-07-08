# Здесь прописываем модель для базы данных пользователей
import sqlite3



with sqlite3.connect(('nvn.db')) as conn:
    cursor = conn.cursor()
    nickname = input('nickname: ')
    user_name = input('user_name: ')
    chat_name = input('chat_name: ')
    cursor.execute("""
    INSERT INTO 'users' (nickname, user_name, chat_name) VALUES (?, ?, ?);
    """, (nickname, user_name, chat_name))



    cursor.execute("SELECT * FROM 'users'")
    result = cursor.fetchall()
    print(result)