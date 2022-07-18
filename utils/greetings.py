def get_greeting_text(user,
                      counter=None,
                      to_user: bool = False
                      ):
    intent = '\t' * 14

    if not to_user:
        text = (f'🎉В группу <b>{user.group_name}</b>  вступил юбилейный пользователь:\n'
                f'{intent}{user.user_name} - {user.user_mention}\n'
                f'🔢 <b>{counter}</b>, 🕐 <u>{user.current_time.strftime("%d.%m.%Y %H:%M:%S")}</u>')
    else:
        text = (f'🎉 Поздравляю, {user.user_mention}, как же удачно вы попали в нужное время и в нужное место!\n'
                f'Вы {user.techinfo.user_counter} участник комьюнити и Вас ждут плюшки и печенюшки!🎉')
    return text


