package chatmembers

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/internal/data"
	"skbot/internal/functions"
	"skbot/internal/menu"
	"skbot/pkg/logging"
	"time"
)

var NewUserID int64

func WithChatMembersDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger, cfg *config.Config) {

	newUser := update.Message.NewChatMembers[0]
	NewUserID = newUser.ID
	chatId := update.Message.Chat.ID
	groupName := update.Message.Chat.Title
	userCount := 0

	logger.Infof("from members NewUserID %d", NewUserID)

	if !newUser.IsBot {
		count, err := bot.GetChatMembersCount(tgbotapi.ChatMemberCountConfig{
			ChatConfig: tgbotapi.ChatConfig{
				ChatID:             chatId,
				SuperGroupUsername: groupName,
			},
		})

		msg := tgbotapi.NewMessage(chatId, fmt.Sprintf("Рады вас приветствовать "+
			"%s! Давайте знакомиться, расскажите нам о себе пожалуйста.\n"+
			"Как вас зовут? \nИз какого вы города? \nЧто вас привело к нам?", newUser.FirstName))

		ans, _ := bot.Send(msg)

		go func() {

			time.Sleep(60 * time.Second)
			_, _ = bot.Send(tgbotapi.NewDeleteMessage(chatId, ans.MessageID))
		}()

		if count%500 == 0 || count%500 == 1 || count%500 == 2 || count%3 == 0 {

			err = functions.AddNewJubileeUser(&newUser, count, update, logger, cfg)
			if err != nil {
				logger.Error(err)
			}
		}

		var newCheckUser data.JubileeUser
		newUsers, err := functions.GetJubileeUsers(cfg)
		if err != nil {
			logger.Error(err)
		}

		for _, user := range newUsers {
			if int64(user.UserID) == NewUserID {
				newCheckUser = user
				userCount++
			}
		}

		if userCount > 1 {
			msg := tgbotapi.NewMessage(cfg.ModersGroupID.ModeratorsGroup,
				fmt.Sprintf("*Внимание!* У нас новый пользователь! Но *найдено совпавдение* с таким ID `%d`, "+
					"рекомендую проверить весь список новых пользователей перед поздравлением.\nВызовите `меню`", newCheckUser.UserID))
			msg.ParseMode = "markdown"
			_, _ = bot.Send(msg)
		}

		if count%500 == 0 || count%3 == 0 {

			moderGroupList, err := functions.GetModeratorsGroup(cfg)
			if err != nil {
				logger.Error(err)
			}

			for _, group := range moderGroupList {

				if group.GroupID == cfg.ModersGroupID.ModeratorsGroup {

					text := fmt.Sprintf("🎉 В группу: %s вступил юбилейный пользователь!\nИмя: %s "+
						"\nНик: @%s, \nНомер вступления: %d. \nВремя вступления %s",
						groupName, newUser.FirstName, newUser.UserName, count,
						time.Now().Format(config.StructDateTimeFormat))
					msg := tgbotapi.NewMessage(group.GroupID, text)
					msg.ReplyMarkup = menu.NewUserCongratulation

					_, _ = bot.Send(msg)

				} else {
					text := fmt.Sprintf("🎉 В группу: %s вступил юбилейный пользователь!\nИмя: %s "+
						"\nНик: @%s, \nНомер вступления: %d. \nВремя вступления %s",
						groupName, newUser.FirstName, newUser.UserName, count,
						time.Now().Format(config.StructDateTimeFormat))

					_, _ = bot.Send(tgbotapi.NewMessage(group.GroupID, text))
				}
			}
		}
	}

}
