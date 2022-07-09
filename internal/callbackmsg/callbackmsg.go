package callbackmsg

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/internal/menu"
	"skbot/internal/textmsg"
	"skbot/pkg/logging"
	"time"
)

func WithCallBackDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger, modGroupId int64) {

	data := update.CallbackQuery.Data

	switch data {

	// menu
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

		// jubilee users
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

					text := fmt.Sprintf("№: `%d` \nГруппа: *%s*\nИмя: *%s*  Ник: *@%s*\nНомер: *%d*  "+
						"Время: *%s* ", user.UserID, user.GroupName, user.UserName, user.UserNick,
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

		// add new moderators group
	case "add_new_mod":

		if update.CallbackQuery.Message.Chat.ID == modGroupId {
			
			newGroupName := textmsg.MesInfo.Message.Chat.Title
			newGroupId := textmsg.MesInfo.Message.Chat.ID
			logger.Info(newGroupName, newGroupId)

			text := fmt.Sprintf("Внимание! Вы подтверждаетете добавление группы: \n  %s  \nв список администраторов.", newGroupName)
			msg := tgbotapi.NewEditMessageText(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID, text)
			msgConf := tgbotapi.NewEditMessageReplyMarkup(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID,
				tgbotapi.NewInlineKeyboardMarkup(tgbotapi.NewInlineKeyboardRow(menu.Button5)))

			_, _ = bot.Send(msg)
			_, _ = bot.Send(msgConf)

			callback := tgbotapi.NewCallback(update.CallbackQuery.ID, "✅")
			if _, err := bot.Request(callback); err != nil {
				logger.Error(err)
			}
		}

	case "add_new_mod_true":

		newGroupName := textmsg.MesInfo.Message.Chat.Title
		newGroupId := textmsg.MesInfo.Message.Chat.ID

		if update.CallbackQuery.Message.Chat.ID == modGroupId {

			b, _, err := functions.AddModeratorsGroup(newGroupId)
			if b && err != nil {
				_, _ = bot.Send(tgbotapi.NewMessage(modGroupId, fmt.Sprintf("Группа %s уже есть.", newGroupName)))
			} else if b && err == nil {

				_, _ = bot.Send(tgbotapi.NewMessage(modGroupId, fmt.Sprintf("Группа %s успешно добавлена.", newGroupName)))
			} else {
				logger.Error(err)
			}

			callback := tgbotapi.NewCallback(update.CallbackQuery.ID, "✅")
			if _, err := bot.Request(callback); err != nil {
				logger.Error(err)
			}
		}
	}
}
