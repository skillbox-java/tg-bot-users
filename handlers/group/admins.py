from telebot.types import CallbackQuery, Message

from config_data.config import USERS_GROUP, ADMINISTRATORS_GROUP_ID
from database.get_user_info import get_user_by_id
from loader import bot
from utils.greetings import get_greeting_text


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('congratulate'))
def button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = get_user_by_id(user_id)
    bot.send_message(USERS_GROUP[0], get_greeting_text(counter=user.usercounter.user_counter,
                                                       user=user,
                                                       to_user=True
                                                       )
                     )

    bot.answer_callback_query(callback_query_id=call.id,
                              text='Письмецо улетело',
                              show_alert=True
                              )
    bot.delete_message(chat_id=call.message.chat.id,
                       message_id=user.usercounter.message_id
                       )


@bot.message_handler(commands=['anniversary_list', ], is_admin_group=ADMINISTRATORS_GROUP_ID)
def anniversary_list(message: Message):
    from datetime import datetime
    # TODO Вывод возможных юбилейных - 500/501/502
    time = datetime.fromtimestamp(message.date).strftime("%d.%m.%Y %H:%M:%S")
    bot.send_message(message.chat.id, f'Время вызова команды{time}')
