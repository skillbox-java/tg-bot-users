from telebot.types import Message
from loader import bot


@bot.message_handler(commands=['luckylist'])
def bot_lucky_list(message: Message):
    # здесь прописываем выгрузку инфо из базы данных и ее вывод.
    # По ТЗ вывод формата:
    # {НазваниеГруппы} {ИмяУчастника} ({НикУчастника}),
    # {порядковыйНомерВступления} {ВремяВступления}
    # “Java разработчик” Василий(ника нет),
    # 500 26.06.22 10: 56
    pass
