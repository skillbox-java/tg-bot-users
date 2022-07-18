from Utils.DBWorker import get_data_from_grant_numbers


async def get_grant_numbers(group_id):
    numbers = await get_data_from_grant_numbers(group_id)
    if not numbers[0][0]:
        return []
    return [int(number) for number in numbers[0][0].split(',')]
