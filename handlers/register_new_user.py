from loader import bot
from database.commands import insert
import datetime
from database.commands import winner_check



@bot.message_handler(content_types=['new_chat_members'])
def handler_new_member(message):
    nickname = message.from_user.username
    user_name = message.from_user.first_name
    chat_id = message.chat.id
    count = bot.get_chat_members_count(message.chat.id)
    user_number = message.from_user.id
    chat_name = message.chat.title
    # dtime_connetion = message.date
    dtime_connetion = datetime.datetime.now()

    #
    # if not winner_check(user_number):
    #     bot.send_message(message.chat.id, f'Нет в is_winner {winner_check(user_number)}')
    # else:
    #     bot.send_message(message.chat.id, f'Уже побеждал {winner_check(user_number)}')



    if not message.from_user.is_bot and not winner_check(user_number): # тут будут еще проверки count % 500 == 0 и проверка есть ли id нового
                                    # пользователя в списке. Нужно создать функцию проверки в базе.
                                    # Если она вернет None, тогда проверка пройдена

        bot.send_message(message.chat.id, 'Не бот и отсутствует в списке победителей')
        insert(
            nickname=message.from_user.username, user_name=message.from_user.first_name,
            user_number=message.from_user.id, dtime=datetime.datetime.now(),
            chat_id=message.chat.id, is_winer=message.from_user.id
        )
    else:
        bot.send_message(message.chat.id, f'Есть в списке победителей{winner_check(user_number)}')











    # Дальше идет кусок кода когда я сам пытался создать базу,
    # когда Колина не работала я его пока оставлю, он мне помогает думать о том как работает база


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






