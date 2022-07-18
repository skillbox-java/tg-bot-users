from telebot.types import CallbackQuery, Message

from config_data.config import USERS_GROUP, ADMINISTRATORS_GROUP_ID
from database.get_user_info import get_user_by_id, get_not_congratulated_users, get_message_ids
from database.save_user_info import write_message_ids_and_restore_deleted_flag, change_delete_and_congratulate_flag
from keyboards.inline.congratulate import congratulate_or_not
from loader import bot
from utils.greetings import get_greeting_text


@bot.message_handler(commands=['anniversary_list', ], is_admin_group=ADMINISTRATORS_GROUP_ID)
def anniversary_list(message: Message):
    users = get_not_congratulated_users()
    messages = list()
    for user in users:
        text = (f'🎉“{user.group_name}” 👤{user.user_name} - {user.user_mention}\n'
                f'🔢{user.techinfo.user_counter} 🕐{user.current_time.strftime("%d.%m.%Y %H:%M:%S")}')
        msg = bot.send_message(chat_id=message.chat.id,
                               text=text,
                               reply_markup=congratulate_or_not(user.user_id)
                               )
        messages.append(msg.message_id)

    write_message_ids_and_restore_deleted_flag(messages)


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('admin_congrats'))
def anniversary_button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = get_user_by_id(user_id)
    bot.send_message(USERS_GROUP[0],
                     get_greeting_text(counter=user.techinfo.user_counter,
                                       user=user,
                                       to_user=True
                                       )
                     )

    query = get_message_ids()
    for i in query:
        bot.delete_message(chat_id=call.message.chat.id,
                           message_id=i.message_id)
    change_delete_and_congratulate_flag(user=user,
                                        related_field=query,
                                        is_congratulated=True
                                        )

    bot.answer_callback_query(callback_query_id=call.id,
                              text='Письмецо улетело',
                              show_alert=True
                              )


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('reject'))
def reject_button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = get_user_by_id(user_id)
    bot.delete_message(chat_id=call.message.chat.id,
                       message_id=user.techinfo.message_id)
    change_delete_and_congratulate_flag(user=user)
