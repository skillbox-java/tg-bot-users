async def get_ids_for_multiple_record(ids: str):
    return [(group_id,) for group_id in ids.split(',')]
