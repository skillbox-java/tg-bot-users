import asyncio
import logging

from aiogram import Bot, Dispatcher
from aiogram.contrib.fsm_storage.memory import MemoryStorage
from aiogram.contrib.fsm_storage.redis import RedisStorage2
from aiogram.contrib.middlewares.logging import LoggingMiddleware
from aiogram.types import AllowedUpdates

from handlers.admin.add_groups_callback import register_add_groups
from handlers.admin.add_numbers_callback import register_add_numbers
from handlers.admin.cancel import register_cancel_menu
from handlers.admin.configure_groups_callback import register_configure_groups
from handlers.admin.configure_numbers_callback import register_configure_numbers
from handlers.admin.delete_from_groups import register_delete_from_groups
from handlers.admin.delete_from_numbers import register_delete_numbers
from handlers.admin.delete_groups_callback import register_delete_groups_cb
from handlers.admin.delete_numbers_callback import register_delete_numbers_cb
from handlers.admin.get_mod_group import register_get_mod_group
from handlers.admin.get_numbers import register_get_grant_numbers
from handlers.admin.get_numbers_group import register_numbers_group
from handlers.admin.get_users_groups import register_get_users_group
from handlers.admin.main_menu import register_main_menu
from handlers.admin.back_to_main_menu import register_back_to_main
from handlers.admin.show_groups_callback import register_show_groups
from handlers.admin.show_numbers_callback import register_show_numbers
from handlers.admin.user import register_user
from handlers.admin.admin import register_admin

from handlers.groups.check import register_check_queue
from handlers.groups.get_granted import register_get_granted
from handlers.groups.restore import register_restore
from handlers.groups.grant_cancel_callback import register_cancel_grant
from handlers.groups.grant_callback import register_grant
from handlers.groups.catch_update import register_catch
from handlers.groups.echo import register_echo

from misc.set_commands import set_default_commands

from filters.moder_group import IsModerGroup
from filters.granted import IsNotGranted
from filters.count import IsGrantCount
from filters.user_group import IsUserGroup
from Utils.DBWorker import create_tables
from config import load_config
from filters.admin import AdminFilter
from filters.group_join import IsGroupJoin

logger = logging.getLogger(__name__)


def register_all_middlewares(dp):
    dp.setup_middleware(LoggingMiddleware())


def register_all_filters(dp):
    dp.filters_factory.bind(AdminFilter)
    dp.filters_factory.bind(IsGroupJoin)
    dp.filters_factory.bind(IsUserGroup)
    dp.filters_factory.bind(IsModerGroup)
    dp.filters_factory.bind(IsGrantCount)
    dp.filters_factory.bind(IsNotGranted)


def register_all_handlers(dp):
    # register_echo(dp)
    register_admin(dp)
    register_user(dp)
    register_catch(dp)
    register_grant(dp)
    register_cancel_grant(dp)
    register_check_queue(dp)
    register_restore(dp)
    register_get_granted(dp)
    register_configure_groups(dp)
    register_show_groups(dp)
    register_add_groups(dp)
    register_get_mod_group(dp)
    register_get_users_group(dp)
    register_delete_groups_cb(dp)
    register_delete_from_groups(dp)
    register_main_menu(dp)
    register_back_to_main(dp)
    register_cancel_menu(dp)
    register_configure_numbers(dp)
    register_show_numbers(dp)
    register_add_numbers(dp)
    register_numbers_group(dp)
    register_get_grant_numbers(dp)
    register_delete_numbers_cb(dp)
    register_delete_numbers(dp)


async def main():
    logging.basicConfig(
        level=logging.INFO,
        format=u'%(filename)s:%(lineno)d #%(levelname)-8s [%(asctime)s] - %(name)s - %(message)s',
    )
    logger.info("Starting bot")
    config = load_config(".env")

    storage = RedisStorage2() if config.tg_bot.use_redis else MemoryStorage()
    bot = Bot(token=config.tg_bot.token, parse_mode='HTML')
    dp = Dispatcher(bot, storage=storage)

    bot['config'] = config
    register_all_middlewares(dp)
    register_all_filters(dp)
    register_all_handlers(dp)
    await set_default_commands(dp)
    await create_tables()

    # start
    try:
        await bot.delete_webhook(drop_pending_updates=True)
        await dp.start_polling(
            allowed_updates=AllowedUpdates.MESSAGE + AllowedUpdates.CHAT_MEMBER + AllowedUpdates.CALLBACK_QUERY)
    finally:
        await dp.storage.close()
        await dp.storage.wait_closed()
        await bot.session.close()


if __name__ == '__main__':
    try:
        asyncio.run(main())
    except (KeyboardInterrupt, SystemExit):
        logger.error("Bot stopped!")
