from Utils.check_number import check_number_in_message


async def check_users_groups(message: str):

    users_groups_ids = message.split(',')
    for number in users_groups_ids:
        if await check_number_in_message(number=number):
            return True

