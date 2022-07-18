from telebot.types import CallbackQuery

from config_data.config import USERS_GROUP
from database.get_user_info import get_user_by_id
from database.save_user_info import change_delete_and_congratulate_flag
from loader import bot
from utils.greetings import get_greeting_text


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('congratulate'))
def button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = get_user_by_id(user_id)
    bot.send_message(USERS_GROUP[0], get_greeting_text(counter=user.techinfo.user_counter,
                                                       user=user,
                                                       to_user=True
                                                       )
                     )

    bot.answer_callback_query(callback_query_id=call.id,
                              text='Письмецо улетело',
                              show_alert=True
                              )
    bot.delete_message(chat_id=call.message.chat.id,
                       message_id=user.techinfo.message_id
                       )
    change_delete_and_congratulate_flag(user=user,
                                        is_congratulated=True
                                        )


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('anniversary_reject'))
def reject_button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = get_user_by_id(user_id)
    bot.delete_message(chat_id=call.message.chat.id,
                       message_id=user.techinfo.message_id)
    change_delete_and_congratulate_flag(user=user)
