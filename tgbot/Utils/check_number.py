
async def check_number_in_message(number: str):
    if number[:1] == '-':
        if number[1:].isdigit():
            return int(number)
    elif number.isdigit():
        return int(number)
