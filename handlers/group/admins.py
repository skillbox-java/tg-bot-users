from telebot.types import CallbackQuery, Message

from config_data.config import USERS_GROUP, ADMINISTRATORS_GROUP_ID, USER_THRESHOLD
from database.get_user_info import get_user_by_id, get_all, get_message_ids
from database.save_user_info import write_message_ids, change_deleted_flag
from keyboards.inline.congratulate import congratulate_userv2
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
    change_deleted_flag(user=user)


@bot.message_handler(commands=['anniversary_list', ], is_admin_group=ADMINISTRATORS_GROUP_ID)
def anniversary_list(message: Message):
    users = get_all()
    messages = list()
    for user in users:
        text = (f'🎉“{user.group_name}” 👤{user.user_name} - {user.user_mention}\n'
                f'🔢{user.usercounter.user_counter}\n'
                f'🕐{user.current_time.strftime("%d.%m.%Y %H:%M:%S")}')
        msg = bot.send_message(chat_id=message.chat.id,
                               text=text,
                               reply_markup=congratulate_userv2(user.user_id)
                               )
        messages.append(msg.message_id)

    write_message_ids(messages)


@bot.callback_query_handler(func=lambda callback: callback.data.startswith('anniversary_list'))
def anniversary_button_pressed(call: CallbackQuery):
    _, user_id = call.data.split(':')
    user = get_user_by_id(user_id)
    bot.send_message(USERS_GROUP[0], get_greeting_text(counter=USER_THRESHOLD,  # TODO Костыль
                                                       user=user,
                                                       to_user=True
                                                       )
                     )
    query = get_message_ids()
    for i in query:
        bot.delete_message(chat_id=call.message.chat.id,
                           message_id=i.message_id)
    change_deleted_flag(related_field=query)

    bot.answer_callback_query(callback_query_id=call.id,
                              text='Письмецо улетело',
                              show_alert=True
                              )
