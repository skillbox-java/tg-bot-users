package callbackmsg

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/internal/menu"
	"skbot/pkg/logging"
	"time"
)

func WithCallBackDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger, modGroupId int64) {

	data := update.CallbackQuery.Data

	switch data {

	case "com_list":

		msg := tgbotapi.NewMessage(update.CallbackQuery.Message.Chat.ID, menu.ComMenu)
		msg.ParseMode = "markdown"
		delMsg, _ := bot.Send(msg)

		callback := tgbotapi.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

		go func() {
			time.Sleep(30 * time.Second)
			_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.CallbackQuery.Message.Chat.ID, delMsg.MessageID))
		}()

	case "jubilee_list":

		var list string
		var count = 1
		users, err := functions.GetJubileeUsers()
		chatId := update.CallbackQuery.Message.Chat.ID
		if err != nil {
			logger.Info(err)
		}

		moderGroupList, err := functions.GetModeratorsGroup()
		if err != nil {
			logger.Error(err)
		}

		for _, group := range moderGroupList {

			if group.GroupID == chatId {

				for _, user := range users {
					// TODO take id from base to number users in list
					text := fmt.Sprintf("*№%d* \nГруппа: *%s*\nИмя: *%s*  Ник: *@%s*\nНомер: *%d*  "+
						"Время: *%s* ", count, user.GroupName, user.UserName, user.UserNick,
						user.Serial, user.Time.Format(config.StructDateTimeFormat))

					list = list + text + "\n\n"
					count++

				}
			}
		}
		msg := tgbotapi.NewMessage(update.CallbackQuery.Message.Chat.ID, "Список юбилейный:\n"+list)
		msg.ParseMode = "markdown"
		_, _ = bot.Send(msg)

		callback := tgbotapi.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

	case "add_mod":

		callback := tgbotapi.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

	}
}
