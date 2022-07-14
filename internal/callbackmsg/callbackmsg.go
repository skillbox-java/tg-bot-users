package callbackmsg

import (
	"fmt"
	tgb "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/chatmembers"
	"skbot/internal/config"
	"skbot/internal/data"
	"skbot/internal/functions"
	"skbot/internal/menu"
	"skbot/internal/textmsg"
	"skbot/pkg/logging"
	"time"
)

func WithCallBackDo(update tgb.Update, bot *tgb.BotAPI, logger *logging.Logger, cfg *config.Config) {

	db, _ := functions.NewFuncList(cfg, logger)
	callBackDoData := update.CallbackQuery.Data

	switch callBackDoData {

	// menu
	case "com_list":

		msg := tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, menu.ComMenu)
		msg.ParseMode = "markdown"
		delMsg, _ := bot.Send(msg)

		callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

		go func() {
			time.Sleep(30 * time.Second)
			_, _ = bot.Send(tgb.NewDeleteMessage(update.CallbackQuery.Message.Chat.ID, delMsg.MessageID))
		}()

		// jubilee users

	case "jubilee_list":

		var list string
		var count = 1
		users, err := db.GetJubileeUsers()
		chatId := update.CallbackQuery.Message.Chat.ID
		if err != nil {
			logger.Info(err)
		}

		moderGroupList, err := db.GetModeratorsGroup()
		if err != nil {
			logger.Error(err)
		}

		for _, group := range moderGroupList {

			if group.ModerGroupID == chatId {

				for _, user := range users {

					text := fmt.Sprintf("№: `%d` \nГруппа: *%s*\nИмя: *%s*  Ник: *@%s*\nНомер: *%d*  "+
						"Время: *%s* ", user.UserID, user.GroupName, user.UserName, user.UserNick,
						user.Serial, user.Time.Format(config.StructDateTimeFormat))

					list = list + text + "\n\n"
					count++

				}
			}
		}
		msg := tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, "Список юбилейный:\n"+list)
		msg.ParseMode = "markdown"
		_, _ = bot.Send(msg)

		callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

		// add new moderators group

	case "all_jubilee_list":

		var list string
		var count = 1
		users, err := db.GetAllJubileeUsers()
		chatId := update.CallbackQuery.Message.Chat.ID
		if err != nil {
			logger.Info(err)
		}

		moderGroupList, err := db.GetModeratorsGroup()
		if err != nil {
			logger.Error(err)
		}

		for _, group := range moderGroupList {

			if group.ModerGroupID == chatId {

				for _, user := range users {

					text := fmt.Sprintf("№: %d, Группа: %s, Имя: %s,  Ник: @%s, Номер: %d, "+
						"Время: %s ", user.UserID, user.GroupName, user.UserName, user.UserNick,
						user.Serial, user.Time.Format(config.StructDateTimeFormat))

					list = list + text + "\n\n"
					count++

				}
			}
		}
		msg := tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, "Список новых пользователей:\n"+list)
		_, _ = bot.Send(msg)

		callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

		// add new moderators group

	case "add_new_mod":

		newGroupName := textmsg.MesInfo.Message.Chat.Title
		newGroupId := textmsg.MesInfo.Message.Chat.ID

		if newGroupId != 0 {
			if update.CallbackQuery.Message.Chat.ID == cfg.ModersGroupID.ModeratorsGroup {

				logger.Info(newGroupName, newGroupId)

				text := fmt.Sprintf("Внимание! Вы подтверждаетете добавление группы: \n  %s  \nв список администраторов.", newGroupName)
				msg := tgb.NewEditMessageText(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID, text)
				msgConf := tgb.NewEditMessageReplyMarkup(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID,
					tgb.NewInlineKeyboardMarkup(tgb.NewInlineKeyboardRow(menu.Button5)))

				_, _ = bot.Send(msg)
				_, _ = bot.Send(msgConf)

				callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
				if _, err := bot.Request(callback); err != nil {
					logger.Error(err)
				}
			}
		} else {
			_, _ = bot.Send(tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, "Время вышло, повторите запрос."))
			_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))
		}

	case "add_new_mod_true":

		newGroupName := textmsg.MesInfo.Message.Chat.Title
		newGroupId := textmsg.MesInfo.Message.Chat.ID

		if newGroupId != 0 {
			if update.CallbackQuery.Message.Chat.ID == cfg.ModersGroupID.ModeratorsGroup {

				text := fmt.Sprintf("Внимание! Вы подтверждаетете добавление группы: \n  %s  \nв список администраторов.", newGroupName)
				msg := tgb.NewEditMessageText(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID, text)
				_, _ = bot.Send(msg)

				b, _, err := db.AddModeratorsGroup(newGroupId, newGroupName)
				if b && err != nil {
					_, _ = bot.Send(tgb.NewMessage(cfg.ModersGroupID.ModeratorsGroup, fmt.Sprintf("Группа %s уже есть.", newGroupName)))
				} else if b && err == nil {

					_, _ = bot.Send(tgb.NewMessage(cfg.ModersGroupID.ModeratorsGroup, fmt.Sprintf("Группа %s успешно добавлена.", newGroupName)))
				} else {
					logger.Error(err)
				}
				textmsg.MesInfo.Message.Chat.ID = 0

				callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
				if _, err := bot.Request(callback); err != nil {
					logger.Error(err)
				}

				textmsg.MesInfo.Message.Chat.ID = 0
			}
		} else {
			_, _ = bot.Send(tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, "Время вышло, повторите запрос."))
			_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))
		}

	case "congratulation_new_user":

		msg := tgb.NewEditMessageText(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID, update.CallbackQuery.Message.Text)
		_, _ = bot.Send(msg)

		var newUser data.JubileeUser
		users, err := db.GetJubileeUsers()
		if err != nil {
			logger.Error(err)
		}
		logger.Infof("from callback new user %d", chatmembers.NewUserID)

		for _, user := range users {

			if int64(user.UserID) == chatmembers.NewUserID {
				newUser = user
			}
		}

		if newUser.UserID != 0 {

			text := fmt.Sprintf(cfg.MsgText.MsgToChatIfNewUser, newUser.UserName, newUser.Serial)
			msg := tgb.NewMessage(newUser.GroupID, text)

			_, _ = bot.Send(msg)

			callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
			if _, err := bot.Request(callback); err != nil {
				logger.Error(err)
			}

		} else {

			msg := tgb.NewMessage(cfg.ModersGroupID.ModeratorsGroup, "ID пользователя == 0! Видимо прошло слишком много времени."+
				"Списки пользователей можно запросить из `меню`.")
			msg.ParseMode = "markdown"
			_, _ = bot.Send(msg)

			callback := tgb.NewCallback(update.CallbackQuery.ID, "❌")
			if _, err := bot.Request(callback); err != nil {
				logger.Error(err)
			}
		}

	case "moderator_group_list":

		var list = "Список групп модераторов и пользователей: "

		groups, err := db.GetModeratorsGroup()
		if err != nil {
			logger.Error(err)
		}

		for _, group := range groups {

			text := fmt.Sprintf("\n№: %d\nМодераторы: %s\nПользователи: %s", group.ID,
				group.ModerGroupTitle, group.UserGroupTitle)

			list = list + text + "\n\n"

		}

		_, _ = bot.Send(tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, list))

		callback := tgb.NewCallback(update.CallbackQuery.ID, "✅")
		if _, err := bot.Request(callback); err != nil {
			logger.Error(err)
		}

	}

}
