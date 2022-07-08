from telebot.types import CallbackQuery

from config_data.config import USERS_GROUP
from loader import bot
from utils.greetings import get_greeting_text
from utils.user_data import UsersData


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('congratulate'))
def button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = UsersData.get_user(user_id=int(user_id))
    bot.send_message(USERS_GROUP[0], get_greeting_text(counter=UsersData.COUNTER,
                                                       user=user,
                                                       to_user=True
                                                       )
                     )

    bot.answer_callback_query(callback_query_id=call.id,
                              text='Письмецо улетело',
                              show_alert=True
                              )
    bot.delete_message(chat_id=call.message.chat.id,
                       message_id=user.message_id
                       )
    UsersData.COUNTER = 0
