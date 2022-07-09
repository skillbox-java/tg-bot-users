package chatmembers

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/pkg/logging"
	"time"
)

func WithChatMembersDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger, cfg *config.Config) {

	newUser := update.Message.NewChatMembers[0]
	chatId := update.Message.Chat.ID
	groupName := update.Message.Chat.Title

	if !newUser.IsBot {
		count, err := bot.GetChatMembersCount(tgbotapi.ChatMemberCountConfig{
			ChatConfig: tgbotapi.ChatConfig{
				ChatID:             chatId,
				SuperGroupUsername: groupName,
			},
		})

		if count%500 == 0 || count%500 == 1 || count%500 == 2 {

			err = functions.AddNewJubileeUser(&newUser, count, groupName, cfg)
			if err != nil {
				logger.Error(err)
			}

			moderGroupList, err := functions.GetModeratorsGroup(cfg)
			if err != nil {
				logger.Error(err)
			}

			for _, group := range moderGroupList {

				text := fmt.Sprintf("🎉 В группу: %s вступил юбилейный пользователь\n%s "+
					"(@%s), %d. \nВремя вступления %s",
					groupName, newUser.FirstName, newUser.UserName, count,
					time.Now().Format(config.StructDateTimeFormat))

				_, _ = bot.Send(tgbotapi.NewMessage(group.GroupID, text))
			}

		}

		msg := tgbotapi.NewMessage(chatId, fmt.Sprintf("_Рады вас приветствовать_ "+
			"*%s*, _расскажите нам о себе пожалуйста._", newUser.FirstName))

		msg.ParseMode = "markdown"
		ans, _ := bot.Send(msg)

		go func() {

			time.Sleep(60 * time.Second)
			_, _ = bot.Send(tgbotapi.NewDeleteMessage(chatId, ans.MessageID))
		}()
	}

}
