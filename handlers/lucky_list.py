from telebot.types import Message
from loader import bot
import database.commands as usersbase
import emoji


winners = usersbase.select2()

@bot.message_handler(commands=['luckylist'])
def bot_lucky_list(message: Message):
    for winner in winners:
        bot.send_message(chat_id=message.chat.id, text=emoji.emojize(f'\U0001F389 {winner[0]} \U0001F464{winner[1]} ({winner[2]}),'
                        f' \n \U0001F522{winner[3]} \U0001F550	{winner[4]}'))

    # здесь прописываем выгрузку инфо из базы данных и ее вывод.
    # По ТЗ вывод формата:
    # {НазваниеГруппы} {ИмяУчастника} ({НикУчастника}),
    # {порядковыйНомерВступления} {ВремяВступления}
    # “Java разработчик” Василий(ника нет),
    # 500 26.06.22 10: 56
    pass
