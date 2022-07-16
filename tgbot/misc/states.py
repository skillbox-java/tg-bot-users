from aiogram.dispatcher.filters.state import StatesGroup, State


class Configure(StatesGroup):

    AddNumbersGroup = State()
    AddNumbers = State()
    DeleteNumbers = State()
    AddModGroups = State()
    AddUserGroups = State()
    DeleteUserGroups = State()
