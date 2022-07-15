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
	"strconv"
	"strings"
	"time"
)

func WithCallBackDo(update tgb.Update, bot *tgb.BotAPI, logger *logging.Logger, cfg *config.Config) {

	db, _ := functions.NewFuncList(cfg, logger)
	callBackDoData := update.CallbackQuery.Data

	callInfo := strings.Split(callBackDoData, " ")

	var localUserId []string

	if callInfo[0] == "congratulation_again" && len(callInfo) > 1 {

		luckyMan, err := strconv.Atoi(callInfo[1])
		if err != nil {
			logger.Error(err)
		}

		jubileeUsers, err := db.GetAllJubileeUsers()
		if err != nil {
			logger.Error(err)
		}

		user := jubileeUsers[luckyMan-1]

		_, _ = bot.Send(tgb.NewMessage(user.GroupID, fmt.Sprintf(cfg.MsgText.MsgToChatIfNewUser, user.UserName, user.Serial)))

		_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

		msg2 := tgb.NewEditMessageText(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID, update.CallbackQuery.Message.Text)
		_, _ = bot.Send(msg2)

	}

	switch callBackDoData {

	// command list
	case "com_list":

		msg := tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, menu.ComMenu)
		msg.ParseMode = "markdown"
		delMsg, _ := bot.Send(msg)

		_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

		go func() {
			time.Sleep(30 * time.Second)
			_, _ = bot.Send(tgb.NewDeleteMessage(update.CallbackQuery.Message.Chat.ID, delMsg.MessageID))
		}()

	// jubilee users
	case "jubilee_list":

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
						"Время: *%s* ", user.ID, user.GroupName, user.UserName, user.UserNick,
						user.Serial, user.Time.UTC().Format(config.StructDateTimeFormat))

					localUserId = append(localUserId, strconv.Itoa(user.ID))
					msg := tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, "Список юбилейный:\n"+text)
					msg.ParseMode = "markdown"
					msg.ReplyMarkup = tgb.NewInlineKeyboardMarkup(
						tgb.NewInlineKeyboardRow(
							tgb.NewInlineKeyboardButtonData("Поздравить", "congratulation_again"+" "+strconv.Itoa(user.ID)),
							tgb.NewInlineKeyboardButtonData("Отклонить", "remove_button"),
						))
					_, _ = bot.Send(msg)

				}
			}
		}

		_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

		// add new moderators group

	case "all_jubilee_list":

		var list string

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

				}
			}
		}

		_, _ = bot.Send(tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, "Список новых пользователей:\n"+list))

		_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

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

				_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))
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

				_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

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

		for _, user := range users {

			if int64(user.UserID) == chatmembers.NewUserID {
				newUser = user
			}
		}

		if newUser.UserID != 0 {

			text := fmt.Sprintf(cfg.MsgText.MsgToChatIfNewUser, newUser.UserName, newUser.Serial)
			msg := tgb.NewMessage(newUser.GroupID, text)

			_, _ = bot.Send(msg)

			_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

		} else {

			msg := tgb.NewMessage(cfg.ModersGroupID.ModeratorsGroup, "ID пользователя == 0! Видимо прошло слишком много времени."+
				"Списки пользователей можно запросить из `меню`.")
			msg.ParseMode = "markdown"

			_, _ = bot.Send(msg)
			_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "❌"))

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

			list = list + text + "\n"

		}

		_, _ = bot.Send(tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, list))

		_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "✅"))

	case "moderator_member":

		_, _ = bot.Send(tgb.NewMessage(update.CallbackQuery.Message.Chat.ID, cfg.MsgText.MsgModeratorMember))

		_, _ = bot.Request(tgb.NewCallback(update.CallbackQuery.ID, "📩"))

	case "remove_button":

		_, _ = bot.Send(tgb.NewEditMessageText(update.CallbackQuery.Message.Chat.ID, update.CallbackQuery.Message.MessageID, update.CallbackQuery.Message.Text))

	}

}
