from telebot.types import Message
from loader import bot
import database.commands as usersbase
import emoji

winners = usersbase.select()
colomns =['Название группы', 'Имя участника', 'Ник участника', 'Порядковый номер вступления', 'Время вступления']
# print(emoji.emojize('Python is :thumbs_up:'))

for winner in winners:
    print(emoji.emojize(f':party popper:{winner[3]} :person raising hand:{winner[2]} ({winner[1]}),'
                        f' \n :	input numbers:{winner[4]} :one o’clock:{winner[5]}'))

@bot.message_handler(commands=['luckylist'])
def bot_lucky_list(message: Message):
    # здесь прописываем выгрузку инфо из базы данных и ее вывод.
    # По ТЗ вывод формата:
    # {НазваниеГруппы} {ИмяУчастника} ({НикУчастника}),
    # {порядковыйНомерВступления} {ВремяВступления}
    # “Java разработчик” Василий(ника нет),
    # 500 26.06.22 10: 56
    pass
