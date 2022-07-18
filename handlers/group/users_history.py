from telebot.types import Message

from config_data.config import ADMINISTRATORS_GROUP_ID
from database.get_user_info import get_all
from database.save_user_info import write_message_ids_and_restore_deleted_flag
from loader import bot


@bot.message_handler(commands=['users_history', ], is_admin_group=ADMINISTRATORS_GROUP_ID)
def users_history(message: Message):
    users = get_all()
    messages = list()
    for user in users:
        congratulated = '👑👑👑' if user.techinfo.congratulated else '🎉'

        text = (f'{congratulated}“{user.group_name}” 👤{user.user_name} - {user.user_mention}\n'
                f'🔢{user.techinfo.user_counter} 🕐{user.current_time.strftime("%d.%m.%Y %H:%M:%S")}')
        msg = bot.send_message(chat_id=message.chat.id,
                               text=text,
                               )
        messages.append(msg.message_id)

    write_message_ids_and_restore_deleted_flag(messages)