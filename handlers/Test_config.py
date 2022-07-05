from loader import bot
@bot.message_handler(content_types=['text'])

def get_text_messages(message):
    if message.text == 'Привет':
        bot.send_message(message.from_user.id, 'Привет, чем я могу тебе помочь?')
    elif message.text == '/help':
        bot.send_message(message.from_user.id, 'Напиши привет')
    # else:
    #     bot.send_message(message.from_user.id, 'Я не понимаю. Напиши /help.')

@bot.message_handler(content_types=['new_chat_members'])

def handler_new_member(message):
    # user_name = message.new_chat_member.first_name
    user_name = message.from_user.first_name

    bot.send_message(message.chat.id, 'Добро пожаловать, {0}'.format(user_name))


