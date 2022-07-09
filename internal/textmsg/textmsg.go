package textmsg

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"log"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/internal/menu"
	"skbot/pkg/logging"
	"strconv"
	"strings"
	"time"
)

var MesInfo tgbotapi.Update

func WithTextQueryDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger, modGroupId int64, cfg *config.Config) {

	// trim symbols
	if len(update.Message.Text) > 0 {

		command, err := functions.TrimSymbolsFromSlice(strings.Split(update.Message.Text, " "))
		if err != nil {
			logger.Info("error trim symbols from message")
		}

		//  menu
		//if strings.Contains(strings.ToLower(command[0]), "меню") {
		//
		//	chatMsgDel := update.Message.MessageID
		//
		//	moderGroupList, err := functions.GetModeratorsGroup()
		//	if err != nil {
		//		logger.Error(err)
		//	}
		//
		//	for _, group := range moderGroupList {
		//
		//		if group.GroupID == update.Message.Chat.ID {
		//
		//			del, _ := bot.Send(tgbotapi.NewMessage(update.Message.Chat.ID, menu.ComMenu))
		//
		//			go func() {
		//				time.Sleep(20 * time.Second)
		//				_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, del.MessageID))
		//				_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, chatMsgDel))
		//			}()
		//		}
		//	}
		//}

		// check jubilee users !
		//if strings.Contains(strings.ToLower(command[0]), "списокюбилейный") {
		//
		//	users, err := functions.GetJubileeUsers()
		//	chatId := update.Message.Chat.ID
		//	if err != nil {
		//		logger.Info(err)
		//	}
		//
		//	moderGroupList, err := functions.GetModeratorsGroup()
		//	if err != nil {
		//		logger.Error(err)
		//	}
		//
		//	for _, group := range moderGroupList {
		//
		//		if group.GroupID == chatId {
		//
		//			for _, user := range users {
		//
		//				text := fmt.Sprintf("Группа: %s\nИмя: %s\nНик: @%s\nНомер вступления: %d\n"+
		//					"Время вступления: %s ", user.GroupName, user.UserName, user.UserNick,
		//					user.Serial, user.Time.Format(config.StructDateTimeFormat))
		//
		//				_, _ = bot.Send(tgbotapi.NewMessage(chatId, text))
		//
		//			}
		//		}
		//	}
		//}

		// add moderator group
		if strings.Contains(strings.ToLower(command[0]), "addmoderatorgroup") {

			if update.Message.Chat.ID == modGroupId {

				newModGroup, err := strconv.ParseInt(command[1], 10, 64)
				if err != nil {
					logger.Error(err)
				}

				b, _, err := functions.AddModeratorsGroup(newModGroup)
				if err != nil {
					logger.Error(err)
				}

				if b && err != nil {

					_, _ = bot.Send(tgbotapi.NewMessage(modGroupId, fmt.Sprintf("Такая группа уже есть: %d", newModGroup)))
				}

				if b && err == nil {

					_, _ = bot.Send(tgbotapi.NewMessage(modGroupId, fmt.Sprintf("Успешно добавлена: %d", newModGroup)))

				}
			}
		}

		//  new users count +
		if strings.Contains(strings.ToLower(command[0]), "chatinfo") {
			delMes := update.Message.MessageID
			chatId := update.Message.Chat.ID
			groupName := update.Message.Chat.Title

			go func() {
				_, _ = bot.Send(tgbotapi.NewDeleteMessage(chatId, delMes))
			}()

			msg := tgbotapi.NewMessage(modGroupId, fmt.Sprintf(
				"ID группы: `%d`\nИмя группы: *%s*", chatId, groupName))
			msg.ParseMode = "markdown"
			_, err = bot.Send(msg)
			if err != nil {
				logger.Error(err)
			}
		}

		// add bad words to the base +
		if strings.Contains(strings.ToLower(command[0]), "мат") {

			if len(command) > 1 {

				go func() {
					time.Sleep(5 * time.Second)
					_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, update.Message.MessageID))

				}()

				b, err := functions.AddBadWord(command[1], cfg)
				if err != nil {
					log.Println(err)
				}

				if b == true && err == nil {

					del, _ := bot.Send(tgbotapi.NewMessage(update.Message.Chat.ID, "Уже есть."))

					go func() {

						time.Sleep(5 * time.Second)
						_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, del.MessageID))

					}()

				} else if b == true && err != nil {

					del, _ := bot.Send(tgbotapi.NewMessage(update.Message.Chat.ID, "Добавлено."))

					go func() {

						time.Sleep(5 * time.Second)
						_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, del.MessageID))
					}()

				}
			}
		}

		// check bad words in chat messages +
		_, b, err := functions.CheckBadWords(command, *cfg)
		if err != nil {
			logger.Error("bad words error", err)
		}

		// message to chat where found bad word, copy to moderators groups +
		if b && strings.ToLower(command[0]) != "мат" {

			msgID := update.Message.MessageID
			badText := update.Message.Text

			go func() {
				_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, msgID))
			}()

			badGuyName := update.Message.From.FirstName
			badGuyNick := update.Message.From.UserName
			badGuyID := update.Message.From.ID
			groupName := update.Message.Chat.Title

			moderatorsGroups, err := functions.GetModeratorsGroup(cfg)
			if err != nil {
				logger.Error(err)
			}
			for _, v := range moderatorsGroups {
				modMess := tgbotapi.NewMessage(v.GroupID, fmt.Sprintf(
					"Замечены нецензурные выражения:\nГруппа: %s\nИмя пользователя: %s\nНик пользователя: "+
						"@%s\nID пользователя: %d\nТекст сообщения: %s\nВремя: %s\nОригинал сообщения удален из чата.",
					groupName, badGuyName, badGuyNick, badGuyID, badText, time.Now().Format(config.StructDateTimeFormat)))

				_, _ = bot.Send(modMess)
			}

			cleanAnswer := tgbotapi.NewMessage(update.Message.Chat.ID, fmt.Sprintf(
				"Уважаемые коллеги, просим вас воздержаться от нецензурных выражений в %s. "+
					"Сообщение удалено, надеемся на ваше понимание.", groupName))
			del, _ := bot.Send(cleanAnswer)

			go func() {
				time.Sleep(30 * time.Second)
				_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, del.MessageID))
			}()
		}

		// menu moderators groups +
		if strings.Contains(strings.ToLower(command[0]), "меню") {

			chatId := update.Message.Chat.ID
			moderatorGroups, err := functions.GetModeratorsGroup(cfg)
			if err != nil {
				return
			}
			for _, group := range moderatorGroups {
				if group.GroupID == chatId {

					_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, update.Message.MessageID))

					msg := tgbotapi.NewMessage(update.Message.Chat.ID, "30 seconds")

					msg.ReplyMarkup = menu.NumericKeyboard

					delMes, err := bot.Send(msg)
					if err != nil {
						logger.Error(err)
					}

					go func() {
						time.Sleep(30 * time.Second)
						_, _ = bot.Send(tgbotapi.NewDeleteMessage(update.Message.Chat.ID, delMes.MessageID))
					}()
				}

			}

		}

		if strings.Contains(strings.ToLower(command[0]), "moder") {

			MesInfo = update
			chatId := update.Message.Chat.ID
			chatName := update.Message.Chat.Title
			userName := update.Message.From.FirstName
			userNick := update.Message.From.UserName

			logger.Info("mes from add moder ", modGroupId)

			text := fmt.Sprintf("Новый запрос на добавление группы администраторов:\nНазвание группы: %s\n"+
				"Уникальный номер группы: %d\nИмя пользователя: %s\nНик пользователя: @%s\nВремя запроса: %s",
				chatName, chatId, userName, userNick, time.Now().Format(config.StructDateTimeFormat))
			msg := tgbotapi.NewMessage(modGroupId, text)
			msg.ReplyMarkup = tgbotapi.NewInlineKeyboardMarkup(tgbotapi.NewInlineKeyboardRow(menu.Button4))
			_, err = bot.Send(msg)
			if err != nil {
				logger.Error(err)
			}

		}

	}

}
