from telebot.types import Message

from config_data.config import USER_THRESHOLD
from keyboards.inline.congratulate import congratulate_user

from loader import bot
from config_data.config import ADMINISTRATORS_GROUP_ID, USERS_GROUP
from utils.greetings import get_greeting_text
from utils.user_data import UsersData

COUNTER = 0


@bot.message_handler(content_types=['text', ], func=lambda msg: msg.chat.id == ADMINISTRATORS_GROUP_ID[0])
def f(message: Message):
    # bot.send_message(message.chat.id, 'Группа админов')
    bot.send_message(message.chat.id,
                     f'id группы <code class="language-python">{message.chat.id}</code>')


@bot.message_handler(content_types=["new_chat_members"], func=lambda msg: msg.chat.id == USERS_GROUP[0])
def new_member(message: Message):
    # global COUNTER
    # COUNTER += 2
    UsersData.COUNTER += 4

    if USER_THRESHOLD == UsersData.COUNTER:
        save_user = UsersData.get_user(message=message)
        text_to_send = get_greeting_text(counter=UsersData.COUNTER,
                                         user=save_user
                                         )
        msg = bot.send_message(chat_id=ADMINISTRATORS_GROUP_ID[0],
                               text=text_to_send,
                               reply_markup=congratulate_user(save_user.user_id),
                               )
        save_user.message_id = msg.message_id
        save_user.user_number = UsersData.COUNTER

        # UsersData.COUNTER = 0


# def get_greeting_text(user: 'UsersData',
#                       to_user: bool = False
#                       ):
#     intent = '\t' * 14
#
#     if not to_user:
#         text = (f'🎉В группу <b>{user.group_name}</b>  вступил юбилейный пользователь:\n'
#                 f'{intent}{user.user_name} - {user.user_mention}\n'
#                 f'🔢 <b>{COUNTER}</b>, 🕐 <u>{user.current_time}</u>')
#     else:
#         text = (f'🎉 Поздравляю, {user.user_mention}, как же удачно вы попали в нужное время и в нужное место!\n'
#                 f'Вы {user.user_number} участник комьюнити и Вас ждут плюшки и печенюшки!🎉')
#     return text


