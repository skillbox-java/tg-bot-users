async def delete_doubles_ids(message: str):
    return ','.join(set(message.replace(' ', '').split(',')))
