package menu

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

const ComMenu = "     Список доступных вам команд:  🛠  \n \n" +
	"✅ `addmoderatorgroup` + номер _(добавление группы модераторов)._\n\n" +
	"✅ `add-moder-group` _(отправляет запрос из группы где есть бот, на добавление этой группы в список групп модераторов)._\n\n" +
	"✅ `add-moder-user-link` _(связывает группу модераторов и пользователей. Введите команду, " +
	"затем через пробел номер группы модераторов, затем через пробел номер группы пользователей. " +
	"Работает только из главной админки)._\n\n" +
	"✅ `chatinfo` _(отправляет информацию в админку о имени и ID группы, откуда отправляется команда" +
	" сообщение будет удалено из группы отправителя, если бот админ)_\n\n" +
	"✅ *Мат + слово* _(Слово будет добавлено в базу)._"

var NumericKeyboard = tgbotapi.NewInlineKeyboardMarkup(
	tgbotapi.NewInlineKeyboardRow(button1),
	tgbotapi.NewInlineKeyboardRow(button2),
	tgbotapi.NewInlineKeyboardRow(button8),
	tgbotapi.NewInlineKeyboardRow(button3),
)

var button1 = tgbotapi.NewInlineKeyboardButtonData("Список команд", "com_list")
var button2 = tgbotapi.NewInlineKeyboardButtonData("Список юбилейный", "jubilee_list")
var button8 = tgbotapi.NewInlineKeyboardButtonData("Весь список новых пользователей ", "all_jubilee_list")
var button3 = tgbotapi.NewInlineKeyboardButtonData("Список групп модераторов и пользователей.", "moderator_group_list")

var Button4 = tgbotapi.NewInlineKeyboardButtonData("Добавить группу", "add_new_mod")
var Button5 = tgbotapi.NewInlineKeyboardButtonData("Да, я уверен!", "add_new_mod_true")

var NewUserCongratulation = tgbotapi.NewInlineKeyboardMarkup(
	tgbotapi.NewInlineKeyboardRow(
		tgbotapi.NewInlineKeyboardButtonData("Поздравить", "congratulation_new_user"),
	))
