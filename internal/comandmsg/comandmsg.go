package comandmsg

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/internal/menu"
	"skbot/pkg/logging"
	"time"
)

func CommandQueryDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger) {

	//  com menu (only moderator's chats) ----------------------------
	if update.Message.Command() == "menu" {

		moderGroupList, err := functions.GetModeratorsGroup()
		if err != nil {
			logger.Error(err)
		}

		for _, group := range moderGroupList {

			if group.GroupID == update.Message.Chat.ID {

				msg := tgbotapi.NewMessage(update.Message.Chat.ID, menu.ComMenu)

				del, _ := bot.Send(msg)

				go func() {
					time.Sleep(60 * time.Second)
					msDel := tgbotapi.NewDeleteMessage(update.Message.Chat.ID, del.MessageID)
					_, _ = bot.Send(msDel)
					msg.ReplyMarkup = tgbotapi.NewRemoveKeyboard(true)

				}()

			}
		}

	}

	// LIST of all jubilee users --------------------------------------
	if update.Message.Command() == "jubileelist" {

		users, err := functions.GetJubileeUsers()
		chatId := update.Message.Chat.ID
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

					text := fmt.Sprintf("*Группа:* _%s_\n*Имя:* %s\n*Ник:* @%s\n*Номер вступления:* %d\n"+
						"*Время вступления:* %s ", user.GroupName, user.UserName, user.UserNick,
						user.Serial, user.Time.Format(config.StructDateTimeFormat))

					msg := tgbotapi.NewMessage(chatId, text)
					msg.ParseMode = "markdown"
					_, _ = bot.Send(msg)

				}
			}
		}

	}

	//if update.Message.Command() == "12" {
	//
	//	msg := tgbotapi.NewMessage(update.Message.Chat.ID, "keyboard")
	//
	//	msg.ReplyMarkup = menu.NumericKeyboard
	//
	//	_, err := bot.Send(msg)
	//	if err != nil {
	//		logger.Error(err)
	//	}
	//
	//	go func() {
	//		time.Sleep(30 * time.Second)
	//		msg.ReplyMarkup = tgbotapi.NewRemoveKeyboard(true)
	//	}()
	//}

}
