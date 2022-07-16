async def delete_doubles_ids(message: str, sort=False):
    without_doubles = set(message.replace(' ', '').split(','))
    if sort:
        for_sorts = map(int, without_doubles)
        return ','.join(map(str, sorted(for_sorts)))
    else:
        return ','.join(without_doubles)
