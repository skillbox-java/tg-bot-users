from aiogram.dispatcher.filters.state import StatesGroup, State


class Configure(StatesGroup):

    AddModGroups = State()
    AddUserGroups = State()
    DeleteUserGroups = State()
