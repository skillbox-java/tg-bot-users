package chatmembers

import (
	"fmt"
	tgb "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/internal/data"
	"skbot/internal/functions"
	"skbot/pkg/logging"
	"strconv"
	"time"
)

var NewUserID int64

func WithChatMembersDo(update tgb.Update, bot *tgb.BotAPI, logger *logging.Logger, cfg *config.Config) {

	db, _ := functions.NewFuncList(cfg, logger)

	newUser := update.Message.NewChatMembers[0]
	NewUserID = newUser.ID
	chatId := update.Message.Chat.ID
	groupName := update.Message.Chat.Title
	userCount := 0

	logger.Infof("from members NewUserID %d", NewUserID)

	if !newUser.IsBot {

		moderGroupList, err := db.GetModeratorsGroup()
		if err != nil {
			logger.Error(err)
		}

		for _, group := range moderGroupList {

			if update.Message.Chat.ID == group.UserGroupID {

				count, err := bot.GetChatMembersCount(tgb.ChatMemberCountConfig{
					ChatConfig: tgb.ChatConfig{
						ChatID:             chatId,
						SuperGroupUsername: groupName,
					},
				})

				msg := tgb.NewMessage(chatId, fmt.Sprintf("Рады вас приветствовать "+
					"%s! Давайте знакомиться, расскажите нам о себе пожалуйста.\n"+
					"Как вас зовут? \nИз какого вы города? \nЧто вас привело к нам?", newUser.FirstName))

				ans, _ := bot.Send(msg)

				go func() {

					time.Sleep(60 * time.Second)
					_, _ = bot.Send(tgb.NewDeleteMessage(chatId, ans.MessageID))
				}()
				// TODO fix count 3
				if count%cfg.Multiplicity == 0 || count%cfg.Multiplicity == 1 || count%cfg.Multiplicity == 2 || count%4 == 0 {

					err = db.AddNewJubileeUser(&newUser, count, update)
					if err != nil {
						logger.Error(err)
					}
				}

				var newCheckUser data.JubileeUser
				newUsers, err := db.GetAllJubileeUsers()
				if err != nil {
					logger.Error(err)
				}

				var congrated string

				for _, user := range newUsers {
					if int64(user.UserID) == NewUserID {
						newCheckUser = user
						userCount++
					}
				}
				if newCheckUser.Marked == 1 {
					congrated = "Уже поздравлен 👑👑👑"
				} else {
					congrated = "Не поздравлен 🎉"
				}

				if userCount > 1 {
					msg := tgb.NewMessage(cfg.ModersGroupID.ModeratorsGroup,
						fmt.Sprintf("*Внимание!* У нас новый пользователь! %s, Но *найдено совпавдение* с таким ID `%d`, "+
							"рекомендую проверить весь список новых пользователей перед поздравлением.\nВызовите `меню`", congrated, newCheckUser.UserID))

					msg.ParseMode = "markdown"
					_, _ = bot.Send(msg)
				}
				//TODO FIX count 3
				if count%cfg.Multiplicity == 0 || count%cfg.Multiplicity == 1 || count%cfg.Multiplicity == 2 || count%4 == 0 {

					for _, group := range moderGroupList {

						if group.ModerGroupID == cfg.ModersGroupID.ModeratorsGroup {

							text := fmt.Sprintf("🎉 В группу: %s вступил юбилейный пользователь!\nИмя: %s "+
								"\nНик: @%s, \nНомер вступления: %d. \nВремя вступления %s",
								groupName, newUser.FirstName, newUser.UserName, count,
								time.Now().Format(config.StructDateTimeFormat))
							msg := tgb.NewMessage(group.ModerGroupID, text)
							msg.ReplyMarkup = tgb.NewInlineKeyboardMarkup(
								tgb.NewInlineKeyboardRow(
									tgb.NewInlineKeyboardButtonData("Поздравить", "congratulation_again"+" "+strconv.Itoa(newCheckUser.ID)),
									tgb.NewInlineKeyboardButtonData("Отклонить", "remove_button"),
								))

							logger.Infof("user ID %d from chatMembers", newCheckUser.ID)

							_, _ = bot.Send(msg)

						} else if update.Message.Chat.ID == group.UserGroupID {
							text := fmt.Sprintf("🎉 В группу: %s вступил юбилейный пользователь!\nИмя: %s "+
								"\nНик: @%s, \nНомер вступления: %d. \nВремя вступления %s",
								groupName, newUser.FirstName, newUser.UserName, count,
								time.Now().Format(config.StructDateTimeFormat))

							_, _ = bot.Send(tgb.NewMessage(group.ModerGroupID, text))
						}
					}
				}
			}
		}
	}
}
