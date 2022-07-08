from telebot import TeleBot
from config_data import config

bot = TeleBot(token=config.BOT_TOKEN, parse_mode='HTML')

