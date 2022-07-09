package menu

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

const ComMenu = "     Список доступных вам команд:  🛠  \n \n" +
	"✅ `addmoderatorgroup` + номер _(добавление группы модераторов)._\n" +
	"✅ `moder` _(отправляет запрос из группы на добавление в список групп модераторов)._\n" +
	"✅ `chatinfo` _(информация о имени и ID группы будет отправлено в админку," +
	" сообщение будет удалено из группы пользователей, если бот админ)_\n" +
	"✅ *Мат + слово* _(Слово будет добавлено в базу)._\n"

var NumericKeyboard = tgbotapi.NewInlineKeyboardMarkup(
	tgbotapi.NewInlineKeyboardRow(button1),
	tgbotapi.NewInlineKeyboardRow(button2),
	tgbotapi.NewInlineKeyboardRow(button3),
)

var button1 = tgbotapi.NewInlineKeyboardButtonData("Список команд", "com_list")
var button2 = tgbotapi.NewInlineKeyboardButtonData("Список юбилейный", "jubilee_list")
var button3 = tgbotapi.NewInlineKeyboardButtonData("Добавить группу администраторов", "add_mod")

var Button4 = tgbotapi.NewInlineKeyboardButtonData("Добавить группу", "add_new_mod")
var Button5 = tgbotapi.NewInlineKeyboardButtonData("Да, я уверен!", "add_new_mod_true")
