import sqlite3

from loader import bot
from database.commands import insert_lite
from database.commands import select



@bot.message_handler(content_types=['text'])
def get_text_messages(message):
    if message.text == 'Привет':
        bot.send_message(message.from_user.id, 'Привет, чем я могу тебе помочь?')
    elif message.text == '/help':
        bot.send_message(message.from_user.id, 'Напиши привет')
    # else:
    #     bot.send_message(message.from_user.id, 'Я не понимаю. Напиши /help.')



@bot.message_handler(content_types=['new_chat_members'])
def handler_new_member(message):
    user_name = message.from_user.first_name
    # user_id = message.chat.id я забыл зачем это было нужно
    count = bot.get_chat_members_count(message.chat.id)
    if count == 6:
        bot.send_message(message.chat.id, 'Нас включая бота, {0}'.format(count))
    #
    # insert_lite(nickname=message.from_user.username, user_name=message.from_user.first_name, chat_name=message.chat.title)
    # result = select()
    # print(result)
    # bot.send_message(message.chat.id, 'Добро пожаловать, {0}'.format(user_name))


    # connect = sqlite3.connect('users.db')
    # cursor = connect.cursor()
    # cursor.execute("""CREATE TABLE IF NOT EXISTS login_id(
    #     id INTEGER
    # )""")
    # connect.commit()
    #
    # people_id = message.chat.id
    # cursor.execute(f'SELECT id FROM login_id WHERE id = {people_id}')
    # data = cursor.fetchone()
    # if data is None:
    #
    #     users_list = [message.chat.id]
    #     cursor.execute('INSERT INTO login_id VALUES (?);', users_list)
    #     connect.commit()






