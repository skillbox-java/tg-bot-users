from telebot.types import Message

from config_data.config import ADMINISTRATORS_GROUP_ID, USERS_GROUP
from config_data.config import USER_THRESHOLD
from database.save_user_info import insert_user_to_db, insert_counter_and_message_id, duplicate
from keyboards.inline.congratulate import notify_about_anniversary_user
from loader import bot
from utils.greetings import get_greeting_text


@bot.message_handler(content_types=["new_chat_members"], is_users_group=USERS_GROUP)
def new_member(message: Message):
    members_count = bot.get_chat_members_count(message.chat.id)

    if (not message.from_user.is_bot
            and not members_count % USER_THRESHOLD
            and not duplicate(message.new_chat_members[0].id)
    ):
        user_obj = insert_user_to_db(message)
        text_to_send = get_greeting_text(counter=members_count,
                                         user=user_obj
                                         )
        msg = bot.send_message(chat_id=ADMINISTRATORS_GROUP_ID[0],
                               text=text_to_send,
                               reply_markup=notify_about_anniversary_user(user_obj.user_id),
                               )
        insert_counter_and_message_id(instance=user_obj,
                                      user_counter=members_count,
                                      message_id=msg.message_id)
    elif (not message.from_user.is_bot
          and not (members_count - 1) % USER_THRESHOLD or not (members_count - 2) % USER_THRESHOLD
          and not duplicate(message.new_chat_members[0].id)):
        user_obj = insert_user_to_db(message)
        insert_counter_and_message_id(instance=user_obj,
                                      user_counter=members_count,
                                      )


